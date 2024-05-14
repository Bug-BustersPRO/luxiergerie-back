package com.luxiergerie.Services;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class RoomPinAuthenticationToken extends AbstractAuthenticationToken {

    private final int roomNumber;
    private final int pin;

    public RoomPinAuthenticationToken(int roomNumber, int pin) {
        super(null);
        this.roomNumber = roomNumber;
        this.pin = pin;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return pin;
    }

    @Override
    public Object getPrincipal() {
        return roomNumber;
    }
}