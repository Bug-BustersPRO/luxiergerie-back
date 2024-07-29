package com.luxiergerie.Mapper;

import com.luxiergerie.DTO.ClientDTO;
import com.luxiergerie.Model.Entity.Client;

import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class ClientMapper {
    public static ClientDTO MappedClientFrom(Client client) {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(client.getId());
        clientDTO.setFirstName(client.getFirstName());
        clientDTO.setLastName(client.getLastName());
        clientDTO.setEmail(client.getEmail());
        clientDTO.setPhoneNumber(client.getPhoneNumber());
        if (Objects.nonNull(client.getRoom())) {
            clientDTO.setRoom(client.getRoom().getId());
            clientDTO.setRole(client.getRoom().getRole());
        }
        if (client.getSojourns() != null) {
            clientDTO.setSojourns(client.getSojourns().stream()
                    .map(SojournMapper::MappedSojournFrom)
                    .collect(toList()));
        }
        return clientDTO;
    }

    public static Client MappedClientFrom(ClientDTO dto) {
        Client client = new Client();
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        client.setEmail(dto.getEmail());
        client.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getSojourns() != null) {
            client.setSojourns(dto.getSojourns().stream()
                    .map(SojournMapper::MappedSojournFrom)
                    .collect(toList()));
        }
        return client;
    }

}