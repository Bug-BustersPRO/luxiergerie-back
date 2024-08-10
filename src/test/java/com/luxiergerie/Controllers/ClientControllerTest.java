package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.ClientDTO;
import com.luxiergerie.Model.Entity.Client;
import com.luxiergerie.Repository.ClientRepository;
import com.luxiergerie.Services.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

public class ClientControllerTest {

    @InjectMocks
    private ClientController clientController;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetClients() {
        Client client = new Client();
        client.setId(UUID.randomUUID());
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setPhoneNumber("1234567890");
        client.setEmail("johnDoe@gmail.com");
        client.setPin(1234);
        client.setRoom(null);
        client.setSojourns(null);
        clientRepository.save(client);

        when(clientRepository.findAll()).thenReturn(List.of(client));

        List<ClientDTO> clients = clientController.getClients();

        assertEquals(1, clients.size());
        assertEquals(client.getId(), clients.getFirst().getId());
    }

    @Test
    public void testCreateClient() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setFirstName("John");
        clientDTO.setLastName("Doe");
        clientDTO.setPhoneNumber("1234567890");
        clientDTO.setEmail("johnDoe@gmail.com");
        clientDTO.setRoom(null);
        clientDTO.setSojourns(null);

        when(clientService.createClient(clientDTO)).thenReturn(clientDTO);
        ResponseEntity<ClientDTO> response = clientController.createClient(clientDTO);

        assertEquals(CREATED, response.getStatusCode());
    }

    @Test
    public void testUpdateClient() {
        UUID clientId = UUID.randomUUID();
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setFirstName("John");
        clientDTO.setLastName("Doe");
        clientDTO.setPhoneNumber("1234567890");
        clientDTO.setEmail("newEmail@gmail.com");
        clientDTO.setRoom(null);
        clientDTO.setSojourns(null);

        when(clientService.updateClient(clientId, clientDTO)).thenReturn(clientDTO);
        ResponseEntity<ClientDTO> response = clientController.updateClient(clientId, clientDTO);

        assertEquals(OK, response.getStatusCode());
    }

    @Test
    public void testAddRoomToClient() {
        UUID clientId = UUID.randomUUID();
        String roleName = "ROLE_GOLD";

        when(clientService.addRoomToClient(clientId, roleName)).thenReturn(new ResponseEntity<>(OK));
        ResponseEntity<?> response = clientController.addRoomToClient(clientId, roleName);

        assertEquals(OK, response.getStatusCode());
    }

    @Test
    public void testDeleteClient() {
        UUID clientId = UUID.randomUUID();

        when(clientService.deleteClient(clientId)).thenReturn(new ResponseEntity<>(OK));
        ResponseEntity<?> response = clientController.deleteClient(clientId);

        assertEquals(OK, response.getStatusCode());
    }

}