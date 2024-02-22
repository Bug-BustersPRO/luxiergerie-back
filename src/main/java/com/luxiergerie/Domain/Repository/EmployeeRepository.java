package com.luxiergerie.Domain.Repository;

import com.luxiergerie.Domain.Entity.Employee;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * This interface represents a repository for managing Employee entities.
 * It extends the JpaRepository interface, providing CRUD operations for Employee entities.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
  /**
   * Finds an Employee by their serial number.
   *
   * @param serialNumber The serial number of the Employee.
   * @return The Employee with the specified serial number, or null if not found.
   */
  Employee findBySerialNumber(String serialNumber);
}