package com.luxiergerie.Controllers.Security;

import com.luxiergerie.Domain.Entity.Employee;
import com.luxiergerie.Domain.Entity.Role;
import com.luxiergerie.Domain.Repository.EmployeeRepository;
import com.luxiergerie.Domain.Repository.RoleRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;


class AuthControllerTest {

    @InjectMocks
    AuthController authController;

    @Mock
    private HttpServletRequest request;
    @Mock
    private PasswordEncoder passwordEncoder;


    @Mock
    private RoleRepository roleRepository;
    @Mock
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldCheckCookieTokenWithToken() {

        Cookie[] expectedCookies = new Cookie[]{new Cookie("jwt-token", "teste-moi!!!")};
        when(request.getCookies()).thenReturn(expectedCookies);

        boolean resultCookie = authController.checkCookieToken(request);


        assertTrue(resultCookie);
    }


    @Test
    public void shouldErrorWhenCookieIsEmpty() {

        Cookie[] expectedCookies = new Cookie[]{};
        when(request.getCookies()).thenReturn(expectedCookies);

        boolean resultCookie = authController.checkCookieToken(request);


        assertFalse(resultCookie);
    }

//    @Test
//    public void should() {
//
//        Role employeeRole = new Role();
//        Employee invalidEmployee = new Employee();
//        employeeRole.setId(UUID.randomUUID());
//        employeeRole.setName("ROLE_EMPLOYEE");
//
//        invalidEmployee.setId(UUID.randomUUID());
//        invalidEmployee.setRoles((List<Role>) employeeRole);
//        invalidEmployee.setFirstName("Jojo");
//        invalidEmployee.setLastName("Bizarre");
//        invalidEmployee.setPassword("shortiiiiiii");
//        employeeRole.getEmployees().add(invalidEmployee);
//        when(roleRepository.findByName("ROLE_EMPLOYEE")).thenReturn(employeeRole);
//
//        assertThrows(ResponseStatusException.class,
//                () -> authController.registerEmployee(invalidEmployee, null),
//                "User must have first name, last name and password must be at least 8 characters long");
//    }

    @Test
    public void should() {
        // Configurer le comportement du mock RoleRepository
        Role employeeRole = new Role();
        employeeRole.setId(UUID.randomUUID());
        employeeRole.setName("ROLE_EMPLOYEE");
        when(roleRepository.findByName("ROLE_EMPLOYEE")).thenReturn(employeeRole);

        // Configurer le comportement du mock PasswordEncoder
        when(passwordEncoder.encode("shortiiiiiii")).thenReturn("hashedPassword");

        // Créer un employé invalide
        Employee invalidEmployee = new Employee();
        invalidEmployee.setId(UUID.randomUUID());
        invalidEmployee.setRoles(new ArrayList<>());
        invalidEmployee.getRoles().add(employeeRole);
        invalidEmployee.setFirstName("Jojo");
        invalidEmployee.setLastName("Bizarre");
        invalidEmployee.setPassword("shortiiiiiii");

        // Appeler la méthode et vérifier l'exception attendue
        assertThrows(NullStatusExce.class,
                () -> authController.registerEmployee(invalidEmployee, null),
                "User must have first name, last name and password must be at least 8 characters long");

        // Vérifier que roleRepository.findByName a été appelé exactement une fois avec le bon argument
        verify(roleRepository, times(1)).findByName("ROLE_EMPLOYEE");
    }




}