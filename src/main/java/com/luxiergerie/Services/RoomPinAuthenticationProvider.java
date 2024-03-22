package com.luxiergerie.Services;

import com.luxiergerie.Domain.Entity.Client;
import com.luxiergerie.Domain.Entity.Room;
import com.luxiergerie.Domain.Repository.ClientRepository;
import com.luxiergerie.Domain.Repository.RoomRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class RoomPinAuthenticationProvider implements AuthenticationProvider {

    private final RoomRepository roomRepository;

    public RoomPinAuthenticationProvider(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        int roomNumber = (int) authentication.getPrincipal();
        int pin = (int) authentication.getCredentials();

        Room room = roomRepository.findByRoomNumber(roomNumber);
        if (room == null || room.getClient() == null || room.getClient().getPin() != pin) {
            return null;
        }
        Client client = room.getClient();

        return new RoomPinAuthenticationToken(roomNumber, pin, client.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RoomPinAuthenticationToken.class.isAssignableFrom(authentication);
    }
}