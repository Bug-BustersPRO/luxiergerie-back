package com.luxiergerie.Mapper;

import com.luxiergerie.DTO.ClientDTO;
import com.luxiergerie.Model.Entity.Client;

import java.util.Objects;
import java.util.stream.Collectors;

public class ClientMapper {
    public static ClientDTO toDTO(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setEmail(client.getEmail());
        dto.setPhoneNumber(client.getPhoneNumber());
        if (Objects.nonNull(client.getRoom())) {
            dto.setRoom(client.getRoom().getId());
            dto.setRole(client.getRoom().getRole());
        }
        if (client.getSojourns() != null) {
            dto.setSojourns(client.getSojourns().stream()
                    .map(SojournMapper::MappedSojournFrom)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public static Client toEntity(ClientDTO dto) {
        Client client = new Client();
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        client.setEmail(dto.getEmail());
        client.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getSojourns() != null) {
            client.setSojourns(dto.getSojourns().stream()
                    .map(SojournMapper::MappedSojournFrom)
                    .collect(Collectors.toList()));
        }
        return client;
    }
}