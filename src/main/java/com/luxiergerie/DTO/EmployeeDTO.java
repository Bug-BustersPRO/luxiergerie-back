package com.luxiergerie.DTO;

import com.luxiergerie.Model.Entity.Role;

import java.util.List;
import java.util.UUID;

public class EmployeeDTO {

    private UUID id;
    private String lastName;

    private String firstName;

    private String serialNumber;

    private String password;

    private List<Role> roles;

    public EmployeeDTO(UUID id, String lastName, String firstName, String serialNumber, String password, List<Role> roles) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.serialNumber = serialNumber;
        this.password = password;
        this.roles = roles;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

}