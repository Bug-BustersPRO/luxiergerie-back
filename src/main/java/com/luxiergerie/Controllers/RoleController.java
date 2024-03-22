package com.luxiergerie.Controllers;

import com.luxiergerie.Domain.Entity.Role;
import com.luxiergerie.Domain.Repository.RoleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
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
    public Role updateRole(@PathVariable UUID id, @RequestBody Role role) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            Role roleToUpdate = roleOptional.get();
            roleToUpdate.setName(role.getName());
            return roleRepository.save(roleToUpdate);
        }
        throw new RuntimeException("Role not found with id : " + id);
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable UUID id) {
        roleRepository.deleteById(id);
    }

}
