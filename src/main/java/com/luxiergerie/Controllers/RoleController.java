package com.luxiergerie.Controllers;

import com.luxiergerie.Model.Entity.Role;
import com.luxiergerie.Repository.RoleRepository;
import com.luxiergerie.Services.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    private final RoleRepository roleRepository;
    private final RoleService roleService;

    public RoleController(RoleRepository roleRepository, RoleService roleService) {
        this.roleRepository = roleRepository;
        this.roleService = roleService;
    }

    @GetMapping("")
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @GetMapping("/{id}")
    public Role getRole(@PathVariable("id") UUID id) {
        UUID nonNullId = requireNonNull(id, "Role ID must not be null");
        return this.roleRepository.findById(nonNullId)
                .orElseThrow(() -> new RuntimeException("Role not found with id : " + nonNullId));
    }

    @PostMapping("")
    public Role addANewRole(@RequestBody Role role) {
        return roleRepository.save(role);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable UUID id, @RequestBody Role role) {
        try {
            roleService.updateRole(id, role);
            return new ResponseEntity<>(OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable UUID id) {
        roleRepository.deleteById(id);
    }

}