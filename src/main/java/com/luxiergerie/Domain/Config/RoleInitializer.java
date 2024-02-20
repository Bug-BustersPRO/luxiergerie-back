package com.luxiergerie.Domain.Config;

import com.luxiergerie.Domain.Entity.Role;
import com.luxiergerie.Domain.Repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleInitializer {

    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("ROLE_EMPLOYEE") == null) {
                Role employeeRole = new Role();
                employeeRole.setName("ROLE_EMPLOYEE");
                roleRepository.save(employeeRole);
            }

            if (roleRepository.findByName("ROLE_ADMIN") == null) {
                Role adminRole = new Role();
                adminRole.setName("ROLE_ADMIN");
                roleRepository.save(adminRole);
            }
        };
    }
}