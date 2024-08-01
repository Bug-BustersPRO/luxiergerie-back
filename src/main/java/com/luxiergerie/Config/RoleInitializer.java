//package com.luxiergerie.Domain.Config;
//
//import com.luxiergerie.Domain.Entity.Role;
//import com.luxiergerie.Domain.Repository.RoleRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * This class initializes the roles in the application.
// */
//@Configuration
//public class RoleInitializer {
//
//  /**
//   * Initializes the roles using the provided RoleRepository.
//   *
//   * @param roleRepository The repository for managing roles.
//   * @return A CommandLineRunner that initializes the roles.
//   */
//  @Bean
//  public CommandLineRunner initRoles(RoleRepository roleRepository) {
//    return args -> {
//      if (roleRepository.findByName("ROLE_EMPLOYEE") == null) {
//        Role employeeRole = new Role();
//        employeeRole.setName("ROLE_EMPLOYEE");
//        roleRepository.save(employeeRole);
//      }
//
//      if (roleRepository.findByName("ROLE_ADMIN") == null) {
//        Role adminRole = new Role();
//        adminRole.setName("ROLE_ADMIN");
//        roleRepository.save(adminRole);
//      }
//
//      if (roleRepository.findByName("ROLE_DIAMOND") == null) {
//        Role diamondRole = new Role();
//        diamondRole.setName("ROLE_DIAMOND");
//        roleRepository.save(diamondRole);
//      }
//
//      if (roleRepository.findByName("ROLE_GOLD") == null) {
//        Role goldRole = new Role();
//        goldRole.setName("ROLE_GOLD");
//        roleRepository.save(goldRole);
//      }
//    };
//  }
//}
// A DECOMMENTER SI ON N'UTILISE PAS UNE DB LOCALE