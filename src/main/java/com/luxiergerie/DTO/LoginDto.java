package com.luxiergerie.DTO;

public class LoginDto {
    private String serialNumber;
    private String password;
    public LoginDto() {      }
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
}
