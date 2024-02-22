package com.luxiergerie.Domain.Repository;

import com.luxiergerie.Domain.Entity.Role;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * The RoleRepository interface provides methods to interact with the database for Role entities.
 */
public interface RoleRepository extends JpaRepository<Role, UUID> {
  /**
   * Finds a role by its name.
   *
   * @param name The name of the role to find.
   * @return The role with the specified name, or null if not found.
   */
  Role findByName(String name);
}
