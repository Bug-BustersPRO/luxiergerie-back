package com.luxiergerie.Domain.Mapper;

import com.luxiergerie.DTO.ClientDTO;
import com.luxiergerie.Domain.Entity.Client;

import java.util.Objects;

public class ClientMapper {
    public static ClientDTO toDTO(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setFirstname(client.getFirstName());
        dto.setLastname(client.getLastName());
        dto.setEmail(client.getEmail());
        dto.setPhoneNumber(client.getPhoneNumber());
        if (Objects.nonNull(client.getRoom())) {
            dto.setRoom(client.getRoom().getId());
            dto.setRole(client.getRoom().getRole());
        }
        return dto;
    }

    public static Client toEntity(ClientDTO dto) {
        Client client = new Client();
        client.setFirstName(dto.getFirstname());
        client.setLastName(dto.getLastname());
        client.setEmail(dto.getEmail());
        client.setPhoneNumber(dto.getPhoneNumber());
        return client;
    }
}