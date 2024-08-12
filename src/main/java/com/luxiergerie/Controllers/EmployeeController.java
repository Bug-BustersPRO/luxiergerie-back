package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.EmployeeDTO;
import com.luxiergerie.Mapper.EmployeeMapper;
import com.luxiergerie.Model.Entity.Employee;
import com.luxiergerie.Model.Entity.Role;
import com.luxiergerie.Repository.EmployeeRepository;
import com.luxiergerie.Repository.RoleRepository;
import com.luxiergerie.Services.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeRepository employeeRepository,
                              RoleRepository roleRepository,
                              EmployeeService employeeService) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeDTO> getEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(EmployeeMapper::MappedEmployeeFrom)
                .collect(toList());
    }

    @GetMapping("/{id}/roles")
    public ResponseEntity<List<Role>> getRolesByEmployeeId(@PathVariable UUID id) {
        try {
            List<Role> roles = employeeService.getRolesByEmployeeId(id);
            return new ResponseEntity<>(roles, OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable("id") UUID id) {
        try {
            EmployeeDTO employeeDTO = employeeService.getEmployeeById(id);
            return new ResponseEntity<>(employeeDTO, OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        try {
            employeeService.createEmployee(employeeDTO);
            return new ResponseEntity<>(CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable("id") UUID id, @RequestBody EmployeeDTO employeeDTO) {
        try {
            employeeService.updateEmployee(id, employeeDTO);
            return new ResponseEntity<>(OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") UUID id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {

            if (e.getMessage().contains("not found")) {
                return new ResponseEntity<>(NOT_FOUND);
            } else if (e.getMessage().contains("permission")) {
                return new ResponseEntity<>(FORBIDDEN);
            } else {
                return new ResponseEntity<>(BAD_REQUEST);
            }
        }
    }

}