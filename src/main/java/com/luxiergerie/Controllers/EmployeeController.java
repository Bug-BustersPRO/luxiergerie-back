package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.EmployeeDTO;
import com.luxiergerie.Domain.Entity.Employee;
import com.luxiergerie.Domain.Entity.Role;
import com.luxiergerie.Domain.Mapper.EmployeeMapper;
import com.luxiergerie.Domain.Repository.EmployeeRepository;
import com.luxiergerie.Domain.Repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.luxiergerie.Domain.Mapper.EmployeeMapper.MappedEmployeeFrom;
import static java.lang.Math.random;
import static java.lang.String.valueOf;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;


/**
 * Controller class for handling employee-related API endpoints.
 */
@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;

    public EmployeeController(EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public List<EmployeeDTO> getEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(EmployeeMapper::MappedEmployeeFrom)
                .collect(toList());
    }

    /**
     * Get roles by employee ID.
     *
     * @param id the employee ID
     * @return the list of roles
     * @throws RuntimeException if employee is not found
     */
    @GetMapping("/{id}/roles")
    public List<Role> getRolesByEmployeeId(@PathVariable UUID id) {
        UUID nonNullId = requireNonNull(id, "Employee ID must not be null");
        Employee employee = this.employeeRepository.findById(nonNullId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + nonNullId));
        return employee.getRoles();
    }

    /**
     * Get employee by ID.
     *
     * @param id the employee ID
     * @return the employee
     * @throws RuntimeException if employee is not found
     */
    @GetMapping("/{id}")
    public EmployeeDTO getEmployeeById(@PathVariable("id") UUID id) {
        UUID nonNullId = requireNonNull(id, "Employee ID must not be null");
        Employee employee = employeeRepository.findById(nonNullId)
                .orElseThrow(() -> new RuntimeException("employee not found with id : " + nonNullId));
        return MappedEmployeeFrom(employee);
    }

    @PostMapping
    public EmployeeDTO createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        String randomInt = valueOf((int) (random() * 10000000));
        employeeDTO.setSerialNumber(randomInt);
        Employee employee = MappedEmployeeFrom(employeeDTO);
        Role role;
        Role employeeRole = employeeDTO.getRoles().getFirst();
        if (employeeRole == this.roleRepository.findByName("ROLE_ADMIN")) {
            role = this.roleRepository.findByName("ROLE_ADMIN");
            role.getEmployees().add(MappedEmployeeFrom(employeeDTO));
        }
        role = this.roleRepository.findByName("ROLE_EMPLOYEE");
        role.getEmployees().add(MappedEmployeeFrom(employeeDTO));

        PasswordEncoder passwordEncoder = createDelegatingPasswordEncoder();
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        Employee savedEmployee = employeeRepository.save(employee);
        return MappedEmployeeFrom(savedEmployee);
    }

    @PutMapping("/{id}")
    public EmployeeDTO updateEmployee(@PathVariable("id") UUID id, EmployeeDTO employeeDTO) {
        UUID nonNullId = requireNonNull(id, "Employee id must not be null");
        Optional<Employee> employeeOptional = employeeRepository.findById(nonNullId);
        if (employeeOptional.isPresent()) {
            Employee employeeToUpdate = employeeOptional.get();
            employeeToUpdate.setLastName(employeeDTO.getLastName());
            employeeToUpdate.setFirstName(employeeDTO.getFirstName());
            employeeToUpdate.setRoles(employeeDTO.getRoles());
            employeeToUpdate.setPassword(employeeDTO.getPassword());
            return MappedEmployeeFrom(employeeToUpdate);
        } else {
            throw new RuntimeException("Employee not found with id: " + nonNullId);
        }
    }

}