package com.luxiergerie.Repository;

import com.luxiergerie.Model.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Employee findBySerialNumber(String serialNumber);

    boolean existsBySerialNumber(String serialNumber);

}