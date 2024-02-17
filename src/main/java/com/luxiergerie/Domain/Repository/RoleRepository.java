package com.luxiergerie.Domain.Repository;

import com.luxiergerie.Domain.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findByName(String name);
}
