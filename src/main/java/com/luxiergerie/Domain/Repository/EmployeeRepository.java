package com.luxiergerie.Domain.Repository;

import com.luxiergerie.Domain.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Employee findBySerialNumber(String serialNumber);
}