package com.luxiergerie.Config.Security.Services;

import com.luxiergerie.Model.Entity.Employee;
import com.luxiergerie.Repository.EmployeeRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toSet;


/**
 * This class implements the UserDetailsService interface and provides the implementation for loading user
 * details by username.
 */
@Service
public class UserDetail implements UserDetailsService {

    EmployeeRepository userRepo;

    UserDetail(EmployeeRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String serialNumber) throws UsernameNotFoundException {
        Employee user = userRepo.findBySerialNumber(serialNumber);
        if (isNull(user)) {
            throw new UsernameNotFoundException("User not exists by Serial Number: " + serialNumber + " !");
        }

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName()))
                .collect(toSet());
        return new User(serialNumber, user.getPassword(), authorities);
    }
}
