package com.luxiergerie.DTO;

public class LoginDto {
    private String serialNumber;
    private String password;
    public LoginDto() {      }
    public String getUsername() {
        return serialNumber;
    }
    public void setUsername(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
