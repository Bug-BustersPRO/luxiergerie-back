package com.luxiergerie.Controllers.Security;

import com.luxiergerie.DTO.LoginClientDTO;
import com.luxiergerie.DTO.LoginDTO;
import com.luxiergerie.Domain.Entity.Client;
import com.luxiergerie.Domain.Entity.Employee;
import com.luxiergerie.Domain.Entity.Role;
import com.luxiergerie.Domain.Entity.Room;
import com.luxiergerie.Domain.Mapper.ClientMapper;
import com.luxiergerie.Domain.Repository.ClientRepository;
import com.luxiergerie.Domain.Repository.EmployeeRepository;
import com.luxiergerie.Domain.Repository.RoleRepository;
import com.luxiergerie.Domain.Repository.RoomRepository;
import com.luxiergerie.Services.BlackListTokenService;
import com.luxiergerie.Services.RoomPinAuthenticationProvider;
import com.luxiergerie.Services.RoomPinAuthenticationToken;
import com.luxiergerie.Services.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static io.micrometer.common.util.StringUtils.isEmpty;
import static java.lang.String.valueOf;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;
import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;


/**
 * Controller class for handling authentication-related endpoints.
 */

/**
 * This class represents the controller for authentication-related operations.
 * It handles registration, login, and logout functionality.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final EmployeeRepository employeeRepository;

    private final AuthenticationManager authenticationManager;
    private final AuthenticationManagerBuilder authBuilder;
    private final RoleRepository roleRepository;
    private final ClientRepository clientRepository;
    private final RoomRepository roomRepository;
    private final TokenService tokenService;
    private final BlackListTokenService blackListTokenService;


    public AuthController(EmployeeRepository employeeRepository,
                          AuthenticationManagerBuilder authBuilder,
                          RoleRepository roleRepository,
                          AuthenticationManager authenticationManager,
                          ClientRepository clientRepository, RoomRepository roomRepository, TokenService tokenService,
                          BlackListTokenService blackListTokenService) {
        this.employeeRepository = employeeRepository;
        this.authBuilder = authBuilder;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.clientRepository = clientRepository;
        this.roomRepository = roomRepository;
        this.tokenService = tokenService;
        this.blackListTokenService = blackListTokenService;

    }

    boolean checkCookieToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    return true;
                }
            }
        }
        return false;

    }

    @PostMapping("/register")
    @ResponseStatus(CREATED)
    public Employee registerEmployee(@Valid @RequestBody Employee employee) {

        Role role;
        if (this.employeeRepository.findAll().isEmpty()) {
            role = this.roleRepository.findByName("ROLE_ADMIN");
            role.getEmployees().add(employee);
        }
        role = this.roleRepository.findByName("ROLE_EMPLOYEE");
        role.getEmployees().add(employee);

        String randomInt = valueOf((int) (Math.random() * 10000000));
        employee.setSerialNumber(randomInt);

        if ((isNull(employee.getFirstName()) || isEmpty(employee.getFirstName()))
                || (isNull(employee.getLastName()) || isEmpty(employee.getLastName()))
                || employee.getPassword().length() <= 7) {
            throw new ResponseStatusException(BAD_REQUEST,
                    "User must have first name, last name and password must be at least 8 characters long");
        }

        PasswordEncoder passwordEncoder = createDelegatingPasswordEncoder();
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        return this.employeeRepository.save(employee);

    }

    @PostMapping("/login")
    ResponseEntity<String> authenticateUser(@RequestBody LoginDTO loginDto, HttpServletResponse response,
                                            HttpServletRequest request) {
        if (checkCookieToken(request)) {
            throw new ResponseStatusException(UNAUTHORIZED, "User must logout before registering");
        }
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getSerialNumber(), loginDto.getPassword()));
        var jwt = tokenService.generateToken(authentication);
        if (jwt == null) {
            throw new ResponseStatusException(UNAUTHORIZED, "Invalid username/password supplied");
        }
        generateCookie(jwt, response);
        getContext().setAuthentication(authentication);

        return new ResponseEntity<>("User login successfully!...", OK);

    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (nonNull(cookies)) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt-token")) {
                    this.blackListTokenService.deleteToken(cookie.getValue());

                    return new ResponseEntity<>("User logout successfully!...", OK);
                }
            }
        }
        return new ResponseEntity<>("You were not logged!", OK);

    }

    @PostMapping("/room/login")
    public ResponseEntity<?> clientLogin(@RequestBody LoginClientDTO loginClientDTO, HttpServletResponse response) {
        try {
            Room room = this.roomRepository.findByRoomNumber(loginClientDTO.getRoomNumber());

            if (isNull(room)) {
                return new ResponseEntity<>("Room not found with number: " + loginClientDTO.getRoomNumber(), NOT_FOUND);
            }

            Client client = room.getClient();

            if (isNull(client)) {
                return new ResponseEntity<>("No client found in room with number: " + loginClientDTO.getRoomNumber(), NOT_FOUND);
            }

            if (client.getPin() != loginClientDTO.getPassword()) {
                return new ResponseEntity<>("Invalid pin code", UNAUTHORIZED);
            }

            this.authBuilder.authenticationProvider(new RoomPinAuthenticationProvider(roomRepository));
            Authentication authentication = authenticationManager.authenticate(
                    new RoomPinAuthenticationToken(loginClientDTO.getRoomNumber(), loginClientDTO.getPassword()));
            String jwt = tokenService.generateToken(authentication);

            generateCookie(jwt, response);

            getContext().setAuthentication(authentication);

            return new ResponseEntity<>(ClientMapper.toDTO(client), OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid room number or password", UNAUTHORIZED);
        }
    }

    @GetMapping("/validate-token")
    public ResponseEntity<String> validateToken(HttpServletRequest request, @RequestHeader("Token") String token){
        if (!Objects.equals(token, "")) {
            if (tokenService.isTokenValidAndNotExpired(token)) {
                return ResponseEntity.ok("Token is valid");
            }
        }

        return ResponseEntity.status(NOT_ACCEPTABLE).body("Token is not valid or expired");
    }

    private void generateCookie(String jwt, HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt-token", jwt);
        cookie.setSecure(true);
        cookie.setHttpOnly(false);
        cookie.getValue();
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
    }

}