package com.luxiergerie.Controllers.Security;

import com.luxiergerie.DTO.LoginDto;
import com.luxiergerie.Domain.Entity.Employee;
import com.luxiergerie.Domain.Entity.Role;
import com.luxiergerie.Domain.Repository.BlackListedTokenRepository;
import com.luxiergerie.Domain.Repository.EmployeeRepository;
import com.luxiergerie.Domain.Repository.RoleRepository;
import com.luxiergerie.Services.BlackListTokenService;
import com.luxiergerie.Services.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


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
    private final RoleRepository roleRepository;
    private final TokenService tokenService;

    private final BlackListTokenService blackListTokenService;


    public AuthController(EmployeeRepository employeeRepository,
                          RoleRepository roleRepository,
                          AuthenticationManager authenticationManager,
                          TokenService tokenService,
                          BlackListTokenService blackListTokenService) {
      this.employeeRepository = employeeRepository;
      this.authenticationManager = authenticationManager;
      this.roleRepository = roleRepository;
      this.tokenService = tokenService;
      this.blackListTokenService = blackListTokenService;
    }


    private boolean checkCookieToken(HttpServletRequest request) {
      Cookie[] cookies = request.getCookies();
      if (cookies != null) {
        for (Cookie cookie : cookies) {
          if (cookie.getName().equals("token")) {
            return true;
          }}
        }
      return false;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Employee registerEmployee(@Valid @RequestBody Employee employee) {

        Role role;
        if (this.employeeRepository.findAll().isEmpty()) {
            role = this.roleRepository.findByName("ROLE_ADMIN");
            role.getEmployees().add(employee);
        }
        role = this.roleRepository.findByName("ROLE_EMPLOYEE");
        role.getEmployees().add(employee);

        String randomInt = String.valueOf((int) (Math.random() * 10000000));
        employee.setSerialNumber(randomInt);

        if (employee.getFirstName() == null || employee.getLastName() == null || employee.getPassword().length() <= 7) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User must have first name, last name and password must be at least 8 characters long");
        }

        PasswordEncoder passwordEncoder
                = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        return this.employeeRepository.save(employee);
    }

    @PostMapping("/login")
    ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto, HttpServletResponse response,
        HttpServletRequest request) {
      if (checkCookieToken(request)) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must logout before registering");
      }
      Authentication authentication = this.authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginDto.getSerialNumber(), loginDto.getPassword()));
      var jwt = tokenService.generateToken(authentication);
      if (jwt == null) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username/password supplied");
      }
      Cookie cookie = new Cookie("jwt-token",jwt);
      cookie.setSecure(true);
      cookie.setHttpOnly(false);
      cookie.getValue();
      cookie.setPath("/");
      cookie.setMaxAge(24 * 60 * 60);
      response.addCookie(cookie);
      SecurityContextHolder.getContext().setAuthentication(authentication);

      return new ResponseEntity<>("User login successfully!...", HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
      Cookie[] cookies = request.getCookies();
      if (cookies != null) {
        for (Cookie cookie : cookies) {
          if (cookie.getName().equals("jwt-token")) {
            this.blackListTokenService.deleteToken(cookie.getValue());

            return new ResponseEntity<>("User logout successfully!...", HttpStatus.OK);
          }
        }
      }
      return new ResponseEntity<>("You were not logged!", HttpStatus.OK);
    }
}