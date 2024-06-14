package com.luxiergerie.DTO;

import com.luxiergerie.Domain.Entity.Role;

import java.util.UUID;

public class EmployeeDTO {

    private UUID id;
    private String lastName;

    private String firstname;

    private String serialNumber;

    private String password;

    private Role role;

    public EmployeeDTO(UUID id, String lastName, String firstname, String serialNumber, String password, Role role) {
        this.id = id;
        this.lastName = lastName;
        this.firstname = firstname;
        this.serialNumber = serialNumber;
        this.password = password;
        this.role = role;
    }

    public EmployeeDTO() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
