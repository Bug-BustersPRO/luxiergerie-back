package com.luxiergerie.Controllers;

import com.luxiergerie.Domain.Entity.Employee;
import com.luxiergerie.Domain.Entity.Role;
import com.luxiergerie.Domain.Repository.EmployeeRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/")
    public List<Employee> getEmployees(Model out) {

        return this.employeeRepository.findAll();
    }

    @GetMapping("/{id}")
    public List<Role> getRoleByEmployee(@PathVariable UUID id) {
        return this.employeeRepository.findById(id).get().getRoles();
    }
}
