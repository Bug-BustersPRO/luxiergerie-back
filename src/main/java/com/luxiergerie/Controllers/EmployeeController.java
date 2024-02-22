package com.luxiergerie.Controllers;

import com.luxiergerie.Domain.Entity.Employee;
import com.luxiergerie.Domain.Entity.Role;
import com.luxiergerie.Domain.Repository.EmployeeRepository;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public List<Employee> getEmployees(Model out) {
      return this.employeeRepository.findAll();
    }

    @GetMapping("/{id}/roles")
    public List<Role> getRolesByEmployeeId(@PathVariable UUID id) {
      UUID nonNullId = Objects.requireNonNull(id, "Employee ID must not be null");
      Employee employee = this.employeeRepository.findById(nonNullId)
          .orElseThrow(() -> new RuntimeException("Employee not found with id: " + nonNullId));
      return employee.getRoles();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable UUID id) {
      UUID nonNullId = Objects.requireNonNull(id, "Employee ID must not be null");
      return this.employeeRepository.findById(nonNullId)
        .orElseThrow(() -> new RuntimeException("Employee not found with id: " + nonNullId));
    }
}
