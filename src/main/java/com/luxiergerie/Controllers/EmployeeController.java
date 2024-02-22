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


/**
 * Controller class for handling employee-related API endpoints.
 */
@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
  private final EmployeeRepository employeeRepository;

  public EmployeeController(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  /**
   * Get all employees.
   *
   * @param out the model object
   * @return the list of employees
   */
  @GetMapping
  public List<Employee> getEmployees(Model out) {
    return this.employeeRepository.findAll();
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
    UUID nonNullId = Objects.requireNonNull(id, "Employee ID must not be null");
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
  public Employee getEmployeeById(@PathVariable UUID id) {
    UUID nonNullId = Objects.requireNonNull(id, "Employee ID must not be null");
    return this.employeeRepository.findById(nonNullId)
        .orElseThrow(() -> new RuntimeException("Employee not found with id: " + nonNullId));
  }
}
