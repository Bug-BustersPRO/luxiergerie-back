package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.ClientDTO;
import com.luxiergerie.Domain.Entity.Client;
import com.luxiergerie.Domain.Entity.Role;
import com.luxiergerie.Domain.Entity.Room;
import com.luxiergerie.Domain.Mapper.ClientMapper;
import com.luxiergerie.Domain.Repository.ClientRepository;
import com.luxiergerie.Domain.Repository.RoleRepository;
import com.luxiergerie.Domain.Repository.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final ClientRepository clientRepository;
    private final RoomRepository roomRepository;
    private final RoleRepository roleRepository;

    public ClientController(ClientRepository clientRepository, RoomRepository roomRepository, RoleRepository roleRepository) {
        this.clientRepository = clientRepository;
        this.roomRepository = roomRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public List<ClientDTO> getClients() {
        return this.clientRepository.findAll().stream().map(ClientMapper::toDTO).collect(Collectors.toList());
    }


    @PostMapping
    public ClientDTO createClient(@RequestBody ClientDTO clientDTO) {
        Client client = ClientMapper.toEntity(clientDTO);
        Client savedClient = this.clientRepository.save(client);
        return ClientMapper.toDTO(savedClient);
    }

    @PostMapping("/add-room/{clientId}/with-role/{roleName}")
    public ResponseEntity<?> addRoomToClient(@PathVariable UUID clientId, @PathVariable String roleName) {
        Role role = this.roleRepository.findByName(roleName);
        if (role == null) {
            return new ResponseEntity<>("Role not found with name: " + roleName, HttpStatus.NOT_FOUND);
        }

        Client client = this.clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            return new ResponseEntity<>("Client not found with id: " + clientId, HttpStatus.NOT_FOUND);
        }

        if (client.getRoom() != null) {
            return new ResponseEntity<>("Client already has a room assigned", HttpStatus.BAD_REQUEST);
        }

        Room room = this.roomRepository.findFirstByRoleAndClientIsNull(role);
        if (room == null) {
            return new ResponseEntity<>("No available room found with role: " + roleName, HttpStatus.NOT_FOUND);
        }

        int pin = (int) (Math.random() * 10000);

       // room.setClient(client);
        client.setRoom(room);
        client.setPin(pin);
        this.clientRepository.save(client);
        this.roomRepository.save(room);

        return new ResponseEntity<>(ClientMapper.toDTO(client), HttpStatus.OK);
    }

}