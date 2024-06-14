package com.luxiergerie.Domain.Mapper;

import com.luxiergerie.DTO.EmployeeDTO;
import com.luxiergerie.Domain.Entity.Employee;

public class EmployeeMapper {

    public static EmployeeDTO MappedEmployeeFrom(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setFirstname(employee.getFirstName());
        employeeDTO.setSerialNumber(employee.getSerialNumber());
        employeeDTO.setPassword(employee.getPassword());
        return employeeDTO;
    }

    public static Employee MappedEmployeeFrom(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setId(employeeDTO.getId());
        employee.setLastName(employeeDTO.getLastName());
        employee.setFirstName(employeeDTO.getFirstname());
        employee.setSerialNumber(employeeDTO.getSerialNumber());
        employee.setPassword(employeeDTO.getPassword());
        return employee;
    }

}