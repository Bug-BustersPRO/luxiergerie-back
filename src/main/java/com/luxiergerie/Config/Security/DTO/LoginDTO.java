package com.luxiergerie.Config.Security.DTO;


public class LoginDTO {

    private String serialNumber;

    private String password;

    public LoginDTO() {
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

}