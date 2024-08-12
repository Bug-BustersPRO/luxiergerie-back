package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.SojournDTO;
import com.luxiergerie.Model.Entity.Client;
import com.luxiergerie.Model.Entity.Room;
import com.luxiergerie.Model.Entity.Sojourn;
import com.luxiergerie.Model.Enums.SojournStatus;
import com.luxiergerie.Repository.RoleRepository;
import com.luxiergerie.Repository.SojournRepository;
import com.luxiergerie.Services.SojournService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;

public class SojournControllerTest {

    @InjectMocks
    private SojournController sojournController;

    @Mock
    private SojournRepository sojournRepository;

    @Mock
    private SojournService sojournService;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetSojourns() {
        Client client = new Client();
        client.setId(UUID.randomUUID());
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setPhoneNumber("1234567890");
        client.setEmail("johnDoe@gmail.com");
        client.setPin(1234);

        Room room = new Room();
        room.setId(UUID.randomUUID());
        room.setRole(roleRepository.findByName("ROLE_GOLD"));
        room.setRoomNumber(1);
        room.setFloor(1);

        List<Sojourn> sojournList = new ArrayList<>();
        Sojourn sojourn = new Sojourn();
        sojourn.setId(UUID.randomUUID());
        sojourn.setEntryDate(LocalDateTime.now().minusDays(1));
        sojourn.setExitDate(LocalDateTime.now().plusDays(1));
        sojourn.setStatus(SojournStatus.RESERVED);
        sojourn.setSojournIdentifier("AB1234");
        sojourn.setPin(1234);
        sojourn.setClient(client);
        sojourn.setRoom(room);
        sojournList.add(sojourn);

        when(sojournRepository.findAll()).thenReturn(sojournList);

        List<SojournDTO> sojourns = sojournController.getSojourns();

        assertEquals(1, sojourns.size());
        assertEquals(sojourn.getId(), sojourns.get(0).getId());
    }

    @Test
    public void testCreateSojourn() {
        SojournDTO sojournDTO = new SojournDTO();
        sojournDTO.setEntryDate(LocalDateTime.now().toLocalDate());
        sojournDTO.setExitDate(LocalDateTime.now().plusDays(1).toLocalDate());
        sojournDTO.setSojournIdentifier("AB1234");
        sojournDTO.setPin(1234);
        sojournDTO.setClientId(UUID.randomUUID());
        sojournDTO.setRoomRole(roleRepository.findByName("ROLE_GOLD"));

        doNothing().when(sojournService).createSojourn(sojournDTO);

        HttpStatus status = sojournController.createSojourn(sojournDTO);

        assertEquals(CREATED, status);
    }

    @Test
    public void testUpdateSojourn() {
        Client client = new Client();
        client.setId(UUID.randomUUID());
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setPhoneNumber("1234567890");
        client.setEmail("test@gmail.fr");
        client.setPin(1234);

        Room room = new Room();
        room.setId(UUID.randomUUID());
        room.setRole(roleRepository.findByName("ROLE_GOLD"));
        room.setRoomNumber(1);
        room.setFloor(1);

        UUID sojournId = UUID.randomUUID();
        SojournDTO sojournDTO = new SojournDTO();
        sojournDTO.setEntryDate(LocalDateTime.now().toLocalDate());
        sojournDTO.setExitDate(LocalDateTime.now().plusDays(1).toLocalDate());
        sojournDTO.setStatus(SojournStatus.RESERVED);
        sojournDTO.setSojournIdentifier("AB1234");
        sojournDTO.setPin(1234);
        sojournDTO.setRoomId(room.getId());
        sojournDTO.setClientId(client.getId());

        Sojourn updatedSojourn = new Sojourn();
        updatedSojourn.setId(sojournId);
        updatedSojourn.setEntryDate(LocalDateTime.now().toLocalDate().atTime(14, 0));
        updatedSojourn.setExitDate(LocalDateTime.now().plusDays(1).toLocalDate().atTime(11, 0));
        updatedSojourn.setStatus(SojournStatus.RESERVED);
        updatedSojourn.setSojournIdentifier("AB1234");
        updatedSojourn.setPin(1234);
        updatedSojourn.setRoom(room);
        updatedSojourn.setClient(client);

        when(sojournService.updateSojourn(sojournId, sojournDTO)).thenReturn(updatedSojourn);

        SojournDTO result = sojournController.updateSojourn(sojournId, sojournDTO);

        assertEquals(sojournId, result.getId());
    }

    @Test
    public void testDeleteSojourn() {
        UUID sojournId = UUID.randomUUID();

        doNothing().when(sojournService).deleteSojourn(sojournId);

        HttpStatus status = sojournController.deleteSojourn(sojournId);

        assertEquals(HttpStatus.NO_CONTENT, status);
    }

    @Test
    public void testRecoverPasswordAndIdentifier() {
        UUID sojournId = UUID.randomUUID();

        doNothing().when(sojournService).recoverPasswordAndIdentifier(sojournId);

        HttpStatus status = sojournController.recoverPasswordAndIdentifier(sojournId);

        assertEquals(HttpStatus.OK, status);
    }

    @Test
    public void testCancelSojourn() {
        UUID sojournId = UUID.randomUUID();
        Sojourn sojourn = new Sojourn();
        sojourn.setId(sojournId);
        sojourn.setRoom(new Room());
        sojourn.setClient(new Client());
        sojourn.setExitDate(LocalDateTime.now().plusDays(1));
        sojourn.setEntryDate(LocalDateTime.now().minusDays(1));
        sojourn.setStatus(SojournStatus.RESERVED);
        sojourn.setSojournIdentifier("AB1234");
        sojourn.setPin(1234);

        when(sojournService.cancelSojourn(sojournId)).thenReturn(sojourn);

        SojournDTO result = sojournController.cancelSojourn(sojournId);

        assertEquals(sojournId, result.getId());
    }

}