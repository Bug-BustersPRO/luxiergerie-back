package com.luxiergerie.Services;

import com.luxiergerie.DTO.ClientDTO;
import com.luxiergerie.Mapper.ClientMapper;
import com.luxiergerie.Model.Entity.Client;
import com.luxiergerie.Model.Entity.Role;
import com.luxiergerie.Model.Entity.Room;
import com.luxiergerie.Repository.ClientRepository;
import com.luxiergerie.Repository.RoleRepository;
import com.luxiergerie.Repository.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.luxiergerie.Mapper.ClientMapper.MappedClientFrom;
import static java.lang.StrictMath.random;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final RoomRepository roomRepository;
    private final RoleRepository roleRepository;
    private final SMSService smsService;
    private final EmailService emailService;

    public ClientService(ClientRepository clientRepository,
                         RoomRepository roomRepository,
                         RoleRepository roleRepository,
                         SMSService smsService,
                         EmailService emailService) {
        this.clientRepository = clientRepository;
        this.roomRepository = roomRepository;
        this.roleRepository = roleRepository;
        this.smsService = smsService;
        this.emailService = emailService;
    }

    @Transactional
    public ClientDTO createClient(ClientDTO clientDTO) {
        Client client = MappedClientFrom(clientDTO);
        Client savedClient = this.clientRepository.save(client);
        return MappedClientFrom(savedClient);
    }

    @Transactional
    public ClientDTO updateClient(UUID clientId, ClientDTO clientDTO) {
        Client client = this.clientRepository.findById(clientId).orElse(null);
        if (isNull(client)) {
            return null;
        }

        client.setFirstName(clientDTO.getFirstName());
        client.setLastName(clientDTO.getLastName());
        client.setPhoneNumber(clientDTO.getPhoneNumber());
        client.setEmail(clientDTO.getEmail());

        Client savedClient = this.clientRepository.save(client);
        return MappedClientFrom(savedClient);
    }

    @Transactional
    public ResponseEntity<?> addRoomToClient(UUID clientId, String roleName) {
        Role role = this.roleRepository.findByName(roleName);
        if (isNull(role)) {
            return new ResponseEntity<>("Role not found with name: " + roleName, NOT_FOUND);
        }

        Client client = this.clientRepository.findById(clientId).orElse(null);
        if (isNull(client)) {
            return new ResponseEntity<>("Client not found with id: " + clientId, NOT_FOUND);
        }

        if (nonNull(client.getRoom())) {
            return new ResponseEntity<>("Client already has a room assigned", BAD_REQUEST);
        }

        Room room = this.roomRepository.findFirstByRoleAndClientIsNull(role);
        if (isNull(room)) {
            return new ResponseEntity<>("No available room found with role: " + roleName, NOT_FOUND);
        }

        int pin = (int) (random() * 10000);

        room.setClient(client);
        client.setPin(pin);
        this.clientRepository.save(client);
        this.roomRepository.save(room);

        // TODO do not send SMS in development, only in production because it costs money, so need to be decommented in production
        /*String messageBody = "Le code d'accès pour accéder à la tablette est : le numéro de chambre " + room.getRoomNumber() + " et le code pin " + pin;
        smsService.sendSMS(client.getPhoneNumber(), messageBody);*/

        String emailSubject = "Code d'accès à la tablette";
        String emailBody = "Le code d'accès pour accéder à la tablette est : le numéro de chambre " + room.getRoomNumber() + " et le code pin " + pin;
        emailService.sendEmail(client.getEmail(), emailSubject, emailBody);

        return new ResponseEntity<>(MappedClientFrom(client), OK);
    }

    @Transactional
    public ResponseEntity<?> deleteClient(UUID clientId) {
        Client client = this.clientRepository.findById(clientId).orElse(null);
        if (isNull(client)) {
            return new ResponseEntity<>("Client not found with id: " + clientId, NOT_FOUND);
        }

        this.clientRepository.delete(client);
        return new ResponseEntity<>(NO_CONTENT);
    }

}