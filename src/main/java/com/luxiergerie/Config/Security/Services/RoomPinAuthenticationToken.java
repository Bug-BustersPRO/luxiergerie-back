package com.luxiergerie.Config.Security.Services;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class RoomPinAuthenticationToken extends AbstractAuthenticationToken {

    private final String sojournIdentifier;
    private final int pin;

    public RoomPinAuthenticationToken(String sojournIdentifier, int pin) {
        super(null);
        this.sojournIdentifier = sojournIdentifier;
        this.pin = pin;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return pin;
    }

    @Override
    public Object getPrincipal() {
        return sojournIdentifier;
    }

}