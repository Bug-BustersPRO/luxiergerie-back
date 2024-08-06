package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.ClientDTO;
import com.luxiergerie.Mapper.ClientMapper;
import com.luxiergerie.Repository.ClientRepository;
import com.luxiergerie.Repository.RoleRepository;
import com.luxiergerie.Repository.RoomRepository;
import com.luxiergerie.Services.ClientService;
import com.luxiergerie.Services.EmailService;
import com.luxiergerie.Services.SMSService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final ClientRepository clientRepository;
    private final ClientService clientService;
    private final RoomRepository roomRepository;
    private final RoleRepository roleRepository;
    private final SMSService smsService;
    private final EmailService emailService;

    public ClientController(ClientRepository clientRepository, RoomRepository roomRepository, RoleRepository roleRepository, EmailService emailService, ClientService clientService) {
        this.clientRepository = clientRepository;
        this.roomRepository = roomRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.smsService = new SMSService();
        this.clientService = clientService;
    }

    @GetMapping
    public List<ClientDTO> getClients() {
        return this.clientRepository.findAll().stream().map(ClientMapper::MappedClientFrom).collect(toList());
    }

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientDTO) {
        try {
            ClientDTO createdClient = clientService.createClient(clientDTO);
            return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable UUID clientId, @RequestBody ClientDTO clientDTO) {
        try {
            ClientDTO updatedClient = clientService.updateClient(clientId, clientDTO);
            if (updatedClient == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(updatedClient);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add-room/{clientId}/with-role/{roleName}")
    public ResponseEntity<?> addRoomToClient(@PathVariable UUID clientId, @PathVariable String roleName) {
        try {
            return clientService.addRoomToClient(clientId, roleName);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<?> deleteClient(@PathVariable UUID clientId) {
        try {
            return clientService.deleteClient(clientId);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}