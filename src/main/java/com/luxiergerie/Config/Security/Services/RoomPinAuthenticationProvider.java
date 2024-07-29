package com.luxiergerie.Config.Security.Services;

import com.luxiergerie.Model.Entity.Sojourn;
import com.luxiergerie.Repository.SojournRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static java.util.Objects.isNull;

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
        if (isNull(sojourn)) {
            throw new AuthenticationException("Sojourn not found") {
            };
        }

        if (sojourn.getPin() != pin) {
            throw new AuthenticationException("Invalid pin") {
            };
        }

        return new RoomPinAuthenticationToken(sojournIdentifier, pin);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RoomPinAuthenticationToken.class.isAssignableFrom(authentication);
    }

}