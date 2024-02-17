package com.luxiergerie.Services;

import com.luxiergerie.Domain.Entity.Employee;
import com.luxiergerie.Domain.Repository.EmployeeRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetail implements UserDetailsService {

    EmployeeRepository userRepo;

    UserDetail(EmployeeRepository userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String serialNumber)throws UsernameNotFoundException {
        Employee user = userRepo.findBySerialNumber(serialNumber);
        if(user==null){
            throw new UsernameNotFoundException("User not exists by Serial Number: "+serialNumber+" !");
        }

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
        return new User(serialNumber, user.getPassword(), authorities);
    }
}
