package com.luxiergerie.Services;

import com.luxiergerie.DTO.SojournDTO;
import com.luxiergerie.Domain.Entity.Client;
import com.luxiergerie.Domain.Entity.Room;
import com.luxiergerie.Domain.Entity.Sojourn;
import com.luxiergerie.Domain.Mapper.SojournMapper;
import com.luxiergerie.Domain.Repository.ClientRepository;
import com.luxiergerie.Domain.Repository.RoomRepository;
import com.luxiergerie.Domain.Repository.SojournRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class SojournService {
    private final ClientRepository clientRepository;
    private final RoomRepository roomRepository;
    private final SojournRepository sojournRepository;

    public SojournService(ClientRepository clientRepository, RoomRepository roomRepository, SojournRepository sojournRepository) {
        this.clientRepository = clientRepository;
        this.roomRepository = roomRepository;
        this.sojournRepository = sojournRepository;
    }

    public Client getClient(UUID clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));
    }

    public Room getRoom(UUID roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
    }

    @Transactional
    public Sojourn createSojourn(SojournDTO sojournDTO) {
        Sojourn sojourn = SojournMapper.toEntity(sojournDTO);
        Client client = this.getClient(sojournDTO.getClientId());
        Room room = this.getRoom(sojournDTO.getRoomId());

        List<Sojourn> existingSojourns = this.sojournRepository.findByRoomAndEntryDateLessThanEqualAndExitDateGreaterThanEqual(room, sojournDTO.getExitDate(), sojournDTO.getEntryDate());
        if (!existingSojourns.isEmpty()) {
            throw new RuntimeException("Room is already booked for the given date");
        }

        sojourn.setClient(client);
        sojourn.setRoom(room);

        client.getSojourns().add(sojourn);
        room.getSojourns().add(sojourn);

        this.sojournRepository.save(sojourn);
        this.clientRepository.save(client);
        this.roomRepository.save(room);

        return sojourn;
    }
}