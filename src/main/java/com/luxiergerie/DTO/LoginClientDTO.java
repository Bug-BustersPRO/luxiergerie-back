package com.luxiergerie.DTO;

public class LoginClientDTO {

    private String sojournIdentifier;
    private int password;

    public String getSojournIdentifier() {
        return sojournIdentifier;
    }

    public void setSojournIdentifier(String sojournIdentifier) {
        this.sojournIdentifier = sojournIdentifier;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }
}
