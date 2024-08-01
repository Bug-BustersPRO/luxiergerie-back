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

    public static Client MappedClientFrom(ClientDTO clientDTO) {
        Client client = new Client();
        client.setFirstName(clientDTO.getFirstName());
        client.setLastName(clientDTO.getLastName());
        client.setEmail(clientDTO.getEmail());
        client.setPhoneNumber(clientDTO.getPhoneNumber());
        if (clientDTO.getSojourns() != null) {
            client.setSojourns(clientDTO.getSojourns().stream()
                    .map(SojournMapper::MappedSojournFrom)
                    .collect(toList()));
        }
        return client;
    }

}