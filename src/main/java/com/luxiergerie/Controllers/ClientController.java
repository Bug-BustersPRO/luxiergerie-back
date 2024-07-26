package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.ClientDTO;
import com.luxiergerie.Model.Entity.Client;
import com.luxiergerie.Model.Entity.Role;
import com.luxiergerie.Model.Entity.Room;
import com.luxiergerie.Mapper.ClientMapper;
import com.luxiergerie.Repository.ClientRepository;
import com.luxiergerie.Repository.RoleRepository;
import com.luxiergerie.Repository.RoomRepository;
import com.luxiergerie.Services.EmailService;
import com.luxiergerie.Services.SMSService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.luxiergerie.Mapper.ClientMapper.*;
import static java.lang.Math.random;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final ClientRepository clientRepository;
    private final RoomRepository roomRepository;
    private final RoleRepository roleRepository;
    private final SMSService smsService;
    private final EmailService emailService;

    public ClientController(ClientRepository clientRepository, RoomRepository roomRepository, RoleRepository roleRepository, EmailService emailService) {
        this.clientRepository = clientRepository;
        this.roomRepository = roomRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.smsService = new SMSService();
    }

    @GetMapping
    public List<ClientDTO> getClients() {
        return this.clientRepository.findAll().stream().map(ClientMapper::toDTO).collect(toList());
    }

    @PostMapping
    public ClientDTO createClient(@RequestBody ClientDTO clientDTO) {
        Client client = toEntity(clientDTO);
        Client savedClient = this.clientRepository.save(client);
        return toDTO(savedClient);
    }

}