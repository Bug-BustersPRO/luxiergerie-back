package com.luxiergerie.Controllers.Security;

import com.luxiergerie.Domain.Entity.Employee;
import com.luxiergerie.Domain.Entity.Role;
import com.luxiergerie.Domain.Repository.EmployeeRepository;
import com.luxiergerie.Domain.Repository.RoleRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;


import static org.junit.jupiter.api.Assertions.*;
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

        Cookie[] expectedCookies = new Cookie[]{new Cookie("token", "teste-moi!!!")};
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


    @Test
    public void shouldThrownExceptionIfLastNameIsNull() throws InterruptedException {
        List<Role> roles = new ArrayList<>();

        Role adminRole = new Role();
        adminRole.setId(UUID.randomUUID());
        adminRole.setName("ROLE_ADMIN");

        Role employeeRole = new Role();
        employeeRole.setId(UUID.randomUUID());
        employeeRole.setName("ROLE_EMPLOYEE");

        roles.add(adminRole);
        roles.add(employeeRole);

        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(adminRole);
        when(roleRepository.findByName("ROLE_EMPLOYEE")).thenReturn(employeeRole);

        // Configurer le comportement du mock PasswordEncoder
        when(passwordEncoder.encode("short")).thenReturn("hashedPassword");

        // Configurer le comportement du mock EmployeeRepository
        when(employeeRepository.findAll()).thenReturn(new ArrayList<>());
        // Créer un employé invalide
        Employee invalidEmployee = new Employee();
        invalidEmployee.setId(UUID.randomUUID());
        invalidEmployee.setRoles(roles);
        invalidEmployee.setFirstName("jojo");
        invalidEmployee.setLastName(null);
        invalidEmployee.setPassword("shortiiiiii");

        // Appeler la méthode et vérifier l'exception attendue
        assertThrows(ResponseStatusException.class,
                () -> authController.registerEmployee(invalidEmployee),
                "User must have first name, last name, and password must be at least 8 characters long");

        // Vérifier que roleRepository.findByName a été appelé exactement une fois avec le bon argument
        verify(roleRepository, times(1)).findByName("ROLE_ADMIN");

    }

    @Test
    public void shouldThrownExceptionIfLastNameIsEmpty() throws InterruptedException {
        // Configurer le comportement du mock RoleRepository
        List<Role> roles = new ArrayList<>();

        Role adminRole = new Role();
        adminRole.setId(UUID.randomUUID());
        adminRole.setName("ROLE_ADMIN");

        Role employeeRole = new Role();
        employeeRole.setId(UUID.randomUUID());
        employeeRole.setName("ROLE_EMPLOYEE");

        roles.add(adminRole);
        roles.add(employeeRole);

        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(adminRole);
        when(roleRepository.findByName("ROLE_EMPLOYEE")).thenReturn(employeeRole);

        // Configurer le comportement du mock PasswordEncoder
        when(passwordEncoder.encode("short")).thenReturn("hashedPassword");

        // Configurer le comportement du mock EmployeeRepository
        when(employeeRepository.findAll()).thenReturn(new ArrayList<>());

        // Créer un employé invalide
        Employee invalidEmployee = new Employee();
        invalidEmployee.setId(UUID.randomUUID());
        invalidEmployee.setRoles(roles);
        invalidEmployee.setFirstName("jojo");
        invalidEmployee.setLastName("");
        invalidEmployee.setPassword("shortiiiiii");

        // Appeler la méthode et vérifier l'exception attendue
        assertThrows(ResponseStatusException.class,
                () -> authController.registerEmployee(invalidEmployee),
                "User must have first name, last name, and password must be at least 8 characters long");

        // Vérifier que roleRepository.findByName a été appelé exactement une fois avec le bon argument
        verify(roleRepository, times(1)).findByName("ROLE_ADMIN");

    }

    @Test
    public void shouldThrownExceptionIfFirstNameIsNull() throws InterruptedException {
        // Configurer le comportement du mock RoleRepository
        List<Role> roles = new ArrayList<>();

        Role adminRole = new Role();
        adminRole.setId(UUID.randomUUID());
        adminRole.setName("ROLE_ADMIN");

        Role employeeRole = new Role();
        employeeRole.setId(UUID.randomUUID());
        employeeRole.setName("ROLE_EMPLOYEE");

        roles.add(adminRole);
        roles.add(employeeRole);

        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(adminRole);
        when(roleRepository.findByName("ROLE_EMPLOYEE")).thenReturn(employeeRole);

        // Configurer le comportement du mock PasswordEncoder
        when(passwordEncoder.encode("short")).thenReturn("hashedPassword");

        // Configurer le comportement du mock EmployeeRepository
        when(employeeRepository.findAll()).thenReturn(new ArrayList<>());

        // Créer un employé invalide
        Employee invalidEmployee = new Employee();
        invalidEmployee.setId(UUID.randomUUID());
        invalidEmployee.setRoles(roles);
        invalidEmployee.setFirstName(null);
        invalidEmployee.setLastName("test");
        invalidEmployee.setPassword("shortiiiiii");

        // Appeler la méthode et vérifier l'exception attendue
        assertThrows(ResponseStatusException.class,
                () -> authController.registerEmployee(invalidEmployee),
                "User must have first name, last name, and password must be at least 8 characters long");

        // Vérifier que roleRepository.findByName a été appelé exactement une fois avec le bon argument
        verify(roleRepository, times(1)).findByName("ROLE_ADMIN");

    }

    @Test
    public void shouldThrownExceptionIfFirstNameIsEmpty() throws InterruptedException {
        // Configurer le comportement du mock RoleRepository
        List<Role> roles = new ArrayList<>();

        Role adminRole = new Role();
        adminRole.setId(UUID.randomUUID());
        adminRole.setName("ROLE_ADMIN");

        Role employeeRole = new Role();
        employeeRole.setId(UUID.randomUUID());
        employeeRole.setName("ROLE_EMPLOYEE");

        roles.add(adminRole);
        roles.add(employeeRole);

        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(adminRole);
        when(roleRepository.findByName("ROLE_EMPLOYEE")).thenReturn(employeeRole);

        // Configurer le comportement du mock PasswordEncoder
        when(passwordEncoder.encode("short")).thenReturn("hashedPassword");

        // Configurer le comportement du mock EmployeeRepository
        when(employeeRepository.findAll()).thenReturn(new ArrayList<>());

        // Créer un employé invalide
        Employee invalidEmployee = new Employee();
        invalidEmployee.setId(UUID.randomUUID());
        invalidEmployee.setRoles(roles);
        invalidEmployee.setFirstName("");
        invalidEmployee.setLastName("test");
        invalidEmployee.setPassword("shortiiiiii");

        // Appeler la méthode et vérifier l'exception attendue
        assertThrows(ResponseStatusException.class,
                () -> authController.registerEmployee(invalidEmployee),
                "User must have first name, last name, and password must be at least 8 characters long");

        // Vérifier que roleRepository.findByName a été appelé exactement une fois avec le bon argument
        verify(roleRepository, times(1)).findByName("ROLE_ADMIN");

    }

    @Test
    public void shouldThrownExceptionIfPasswordIsTooShort() throws InterruptedException {
        // Configurer le comportement du mock RoleRepository
        List<Role> roles = new ArrayList<>();

        Role adminRole = new Role();
        adminRole.setId(UUID.randomUUID());
        adminRole.setName("ROLE_ADMIN");

        Role employeeRole = new Role();
        employeeRole.setId(UUID.randomUUID());
        employeeRole.setName("ROLE_EMPLOYEE");

        roles.add(adminRole);
        roles.add(employeeRole);

        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(adminRole);
        when(roleRepository.findByName("ROLE_EMPLOYEE")).thenReturn(employeeRole);

        // Configurer le comportement du mock PasswordEncoder
        when(passwordEncoder.encode("short")).thenReturn("hashedPassword");

        // Configurer le comportement du mock EmployeeRepository
        when(employeeRepository.findAll()).thenReturn(new ArrayList<>());

        // Créer un employé invalide
        Employee invalidEmployee = new Employee();
        invalidEmployee.setId(UUID.randomUUID());
        invalidEmployee.setRoles(roles);
        invalidEmployee.setFirstName("jojo");
        invalidEmployee.setLastName("test");
        invalidEmployee.setPassword("short");

        // Appeler la méthode et vérifier l'exception attendue
        assertThrows(ResponseStatusException.class,
                () -> authController.registerEmployee(invalidEmployee),
                "User must have first name, last name, and password must be at least 8 characters long");

        // Vérifier que roleRepository.findByName a été appelé exactement une fois avec le bon argument
        verify(roleRepository, times(1)).findByName("ROLE_ADMIN");

    }

    @Test
    public void shouldCreateAdminEmployee() {
        // Configurer le comportement du mock RoleRepository
        List<Role> roles = new ArrayList<>();

        Role adminRole = new Role();
        adminRole.setId(UUID.randomUUID());
        adminRole.setName("ROLE_ADMIN");

        Role employeeRole = new Role();
        employeeRole.setId(UUID.randomUUID());
        employeeRole.setName("ROLE_EMPLOYEE");

        roles.add(adminRole);
        roles.add(employeeRole);

        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(adminRole);
        when(roleRepository.findByName("ROLE_EMPLOYEE")).thenReturn(employeeRole);

        // Configurer le comportement du mock PasswordEncoder
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        // Configurer le comportement du mock EmployeeRepository
        when(employeeRepository.findAll()).thenReturn(new ArrayList<>());

        // Créer un employé invalide
        Employee adminEmployee = new Employee();
        adminEmployee.setId(UUID.randomUUID());
        adminEmployee.setRoles(roles);
        adminEmployee.setFirstName("Admin");
        adminEmployee.setLastName("User");
        adminEmployee.setPassword("adminPassword");

        // Appeler la méthode et vérifier que l'employé a le rôle admin
        authController.registerEmployee(adminEmployee);

        // Vérifier que roleRepository.findByName a été appelé exactement une fois avec le bon argument
        verify(roleRepository, times(1)).findByName("ROLE_ADMIN");
        verify(roleRepository, times(1)).findByName("ROLE_EMPLOYEE");

        // Vérifier que l'employé a été ajouté au rôle admin
        assertTrue(adminRole.getEmployees().contains(adminEmployee));
    }

    @Test
    void testRegisterEmployeeWithValidData() {

        List<Role> roles = new ArrayList<>();

        Role adminRole = new Role();
        adminRole.setId(UUID.randomUUID());
        adminRole.setName("ROLE_ADMIN");

        Role employeeRole = new Role();
        employeeRole.setId(UUID.randomUUID());
        employeeRole.setName("ROLE_EMPLOYEE");

        roles.add(adminRole);
        roles.add(employeeRole);
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(adminRole);
        when(roleRepository.findByName("ROLE_EMPLOYEE")).thenReturn(employeeRole);

        Employee mockEmployee = new Employee();
        mockEmployee.setId(UUID.randomUUID());
        mockEmployee.setFirstName("John");
        mockEmployee.setLastName("Doe");
        mockEmployee.setPassword("password");
        mockEmployee.setRoles(roles);

        authController.registerEmployee(mockEmployee);

        verify(employeeRepository, times(1)).save(mockEmployee);

        for (Role role : roles) {
            assertTrue(role.getEmployees().contains(mockEmployee));
            assertNotNull(mockEmployee.getSerialNumber());
        }
    }
}