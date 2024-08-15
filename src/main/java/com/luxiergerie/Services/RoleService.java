package com.luxiergerie.Services;

import com.luxiergerie.Model.Entity.Role;
import com.luxiergerie.Repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
import java.util.UUID;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public Role updateRole(@PathVariable UUID id, @RequestBody Role role) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            Role roleToUpdate = roleOptional.get();
            roleToUpdate.setName(role.getName());
            return roleRepository.save(roleToUpdate);
        }
        throw new RuntimeException("Role not found with id : " + id);
    }

}