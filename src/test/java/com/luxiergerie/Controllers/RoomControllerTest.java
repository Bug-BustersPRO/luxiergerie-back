package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.RoomDTO;
import com.luxiergerie.Model.Entity.Client;
import com.luxiergerie.Model.Entity.Employee;
import com.luxiergerie.Model.Entity.Role;
import com.luxiergerie.Model.Entity.Room;
import com.luxiergerie.Repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class RoomControllerTest {
    @InjectMocks
    private RoomController roomController;

    @Mock
    private RoomRepository roomRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getRooms() {
        List<RoomDTO> roomDTOs = new ArrayList<>();
        List<Room> rooms = new ArrayList<>();
        List<Role> roles = new ArrayList<>();

        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        Role adminRole = new Role();
        adminRole.setId(id1);
        adminRole.setName("ROLE_ADMIN");

        Role roomRole = new Role();
        roomRole.setId(id2);
        roomRole.setName("ROLE_GOLD");

        roles.add(adminRole);
        roles.add(roomRole);

        Employee employee = new Employee();
        employee.setId(id1);
        employee.setFirstName("John");
        employee.setLastName("D'où?");
        employee.setPassword("12345678");
        employee.setSerialNumber("azerty01");
        employee.setRoles(roles);

        Client client = new Client();
        client.setId(id1);
        client.setFirstName("Maxime");
        client.setLastName("Devil");

        RoomDTO roomDTO1 = new RoomDTO();
        roomDTO1.setId(id1);
        roomDTO1.setRoomNumber(1);
        roomDTO1.setFloor(1);
        roomDTO1.setRole(roomRole);
        roomDTO1.setClient(client);

        RoomDTO roomDTO2 = new RoomDTO();
        roomDTO2.setId(id2);
        roomDTO2.setRoomNumber(2);
        roomDTO2.setFloor(1);
        roomDTO2.setRole(roomRole);
        roomDTO2.setClient(client);

        roomDTOs.add(roomDTO1);
        roomDTOs.add(roomDTO2);

        Room room1 = new Room();
        room1.setId(id1);
        room1.setRoomNumber(1);
        room1.setFloor(1);
        room1.setRole(roomRole);
        room1.setClient(client);

        Room room2 = new Room();
        room2.setId(id2);
        room2.setRoomNumber(2);
        room2.setFloor(1);
        room2.setRole(roomRole);
        room2.setClient(client);

        rooms.add(room1);
        rooms.add(room2);

        when(roomRepository.findAll()).thenReturn(rooms);
        List<RoomDTO> newList = roomController.getRooms();

        assertEquals(roomDTOs, newList);
    }

    @Test
    public void getAvailableRooms() {
        List<Room> roomsWithClients = new ArrayList<>();
        List<Room> roomsWithoutClients = new ArrayList<>();

        Room roomWithClient = new Room();
        roomWithClient.setClient(new Client());
        roomsWithClients.add(roomWithClient);

        Room roomWithoutClient = new Room();
        roomsWithoutClients.add(roomWithoutClient);

        when(roomRepository.findAllRoomByClientIsNull()).thenReturn(roomsWithoutClients);

        List<RoomDTO> availableRooms = roomController.getAvailableRooms();

        assertEquals(roomsWithoutClients.size(), availableRooms.size());
    }

}
