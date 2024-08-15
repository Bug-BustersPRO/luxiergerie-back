package com.luxiergerie.Mapper;

import com.luxiergerie.DTO.EmployeeDTO;
import com.luxiergerie.Model.Entity.Employee;

public class EmployeeMapper {

    public static EmployeeDTO MappedEmployeeFrom(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setSerialNumber(employee.getSerialNumber());
        employeeDTO.setRoles(employee.getRoles());
        employeeDTO.setPassword(employee.getPassword());
        return employeeDTO;
    }

    public static Employee MappedEmployeeFrom(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setId(employeeDTO.getId());
        employee.setLastName(employeeDTO.getLastName());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setSerialNumber(employeeDTO.getSerialNumber());
        employee.setRoles(employeeDTO.getRoles());
        employee.setPassword(employeeDTO.getPassword());
        return employee;
    }

}