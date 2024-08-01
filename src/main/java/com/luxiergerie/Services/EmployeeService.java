package com.luxiergerie.Services;

import com.luxiergerie.DTO.EmployeeDTO;
import com.luxiergerie.Model.Entity.Employee;
import com.luxiergerie.Model.Entity.Role;
import com.luxiergerie.Repository.EmployeeRepository;
import com.luxiergerie.Repository.RoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.luxiergerie.Mapper.EmployeeMapper.MappedEmployeeFrom;
import static java.lang.Math.random;
import static java.lang.String.valueOf;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;

    public EmployeeService(EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public List<Role> getRolesByEmployeeId(UUID id) {
        UUID nonNullId = requireNonNull(id, "Employee ID must not be null");
        Employee employee = employeeRepository.findById(nonNullId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + nonNullId));
        return employee.getRoles();
    }

    @Transactional
    public EmployeeDTO getEmployeeById(UUID id) {
        UUID nonNullId = requireNonNull(id, "Employee ID must not be null");
        Employee employee = employeeRepository.findById(nonNullId)
                .orElseThrow(() -> new RuntimeException("employee not found with id : " + nonNullId));
        return MappedEmployeeFrom(employee);
    }

    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        String randomInt = valueOf((int) (random() * 10000000));
        employeeDTO.setSerialNumber(randomInt);
        Employee employee = MappedEmployeeFrom(employeeDTO);

        List<Role> roles = employeeDTO.getRoles().stream()
                .map(role -> this.roleRepository.findByName(role.getName()))
                .collect(toList());
        employeeDTO.setRoles(roles);

        Optional<Role> roleById = this.roleRepository.findById(roles.getFirst().getId());
        if (roleById.isPresent()) {
            roleById.get().getEmployees().add(MappedEmployeeFrom(employeeDTO));
        }

        PasswordEncoder passwordEncoder = createDelegatingPasswordEncoder();
        employeeDTO.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        Employee savedEmployee = employeeRepository.save(MappedEmployeeFrom(employeeDTO));

        return MappedEmployeeFrom(savedEmployee);
    }

    @Transactional
    public EmployeeDTO updateEmployee(@PathVariable("id") UUID id, @RequestBody EmployeeDTO employeeDTO) {
        UUID nonNullId = requireNonNull(id, "Employee id must not be null");
        Optional<Employee> employeeOptional = employeeRepository.findById(nonNullId);
        if (employeeDTO.getRoles().getFirst().getName().equals("ROLE_EMPLOYEE")) {

            if (employeeOptional.isPresent()) {
                Employee employeeToUpdate = employeeOptional.get();
                employeeToUpdate.setLastName(employeeDTO.getLastName());
                employeeToUpdate.setFirstName(employeeDTO.getFirstName());

                List<Role> roles = employeeDTO.getRoles().stream()
                        .map(role -> this.roleRepository.findById(role.getId())
                                .orElseThrow(() -> new RuntimeException("Role not found with id: " + role.getId())))
                        .collect(toList());
                employeeToUpdate.setRoles(roles);

                PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
                employeeToUpdate.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));

                Employee savedEmployee = employeeRepository.save(employeeToUpdate);

                return MappedEmployeeFrom(savedEmployee);
            } else {
                throw new RuntimeException("Employee not found with id: " + nonNullId);
            }
        } else {
            throw new RuntimeException("You don't have the role admin for this action");
        }
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") UUID id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found with id: " + id);
        }

        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employee.getRoles().getFirst().getName().equals("ROLE_EMPLOYEE")) {
            employee.getRoles().forEach(role -> role.getEmployees().remove(employee));
            employee.setRoles(null);

            employeeRepository.deleteById(id);

            return ResponseEntity.ok().build();
        }
        throw new RuntimeException("You don't have the permission for this action");
    }

}