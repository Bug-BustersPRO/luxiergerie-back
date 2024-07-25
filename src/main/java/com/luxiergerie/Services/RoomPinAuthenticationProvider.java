package com.luxiergerie.Services;

import com.luxiergerie.Domain.Entity.Client;
import com.luxiergerie.Domain.Entity.Room;
import com.luxiergerie.Domain.Entity.Sojourn;
import com.luxiergerie.Domain.Repository.RoomRepository;
import com.luxiergerie.Domain.Repository.SojournRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Objects;

public class RoomPinAuthenticationProvider implements AuthenticationProvider {

    private final SojournRepository sojournRepository;

    public RoomPinAuthenticationProvider(SojournRepository sojournRepository) {
        this.sojournRepository = sojournRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String sojournIdentifier = (String) authentication.getPrincipal();
        int pin = (int) authentication.getCredentials();

        Sojourn sojourn = sojournRepository.findBySojournIdentifier(sojournIdentifier);
        if (sojourn == null) {
            throw new AuthenticationException("Sojourn not found") {};
        }

        if (sojourn.getPin() != pin) {
            throw new AuthenticationException("Invalid pin") {};
        }

        return new RoomPinAuthenticationToken(sojournIdentifier, pin);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RoomPinAuthenticationToken.class.isAssignableFrom(authentication);
    }
}