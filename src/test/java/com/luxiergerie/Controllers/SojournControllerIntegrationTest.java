package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.SojournDTO;
import com.luxiergerie.Model.Entity.Client;
import com.luxiergerie.Model.Entity.Room;
import com.luxiergerie.Model.Entity.Sojourn;
import com.luxiergerie.Repository.ClientRepository;
import com.luxiergerie.Repository.RoleRepository;
import com.luxiergerie.Repository.RoomRepository;
import com.luxiergerie.Repository.SojournRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static com.luxiergerie.Model.Enums.SojournStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class SojournControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SojournRepository sojournRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ClientRepository clientRepository;

    private Sojourn existingSojourn;
    private Client existingClient;
    private Room existingRoom;

    @BeforeEach
    void setUp() {
        sojournRepository.deleteAll(); // Nettoie la base de données avant chaque test

        // Préparez les données de test
        Client client = new Client();
        client.setId(UUID.randomUUID());
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setPhoneNumber("1234567890");
        client.setEmail("johnDoe@gmail.com");
        client.setPin(1234);
        existingClient = clientRepository.save(client);

        Room room = new Room();
        room.setId(UUID.randomUUID());
        room.setRoomNumber(1);
        room.setFloor(1);
        room.setRole(roleRepository.findByName("ROLE_GOLD"));
        existingRoom = roomRepository.save(room);

        Sojourn sojourn = new Sojourn();
        sojourn.setId(UUID.randomUUID());
        sojourn.setEntryDate(LocalDate.now().minusDays(1).atTime(14, 0));
        sojourn.setExitDate(LocalDate.now().plusDays(1).atTime(11, 0));
        sojourn.setStatus(RESERVED);
        sojourn.setSojournIdentifier("AB1234");
        sojourn.setPin(1234);
        sojourn.setClient(client);
        sojourn.setRoom(room);
        existingSojourn = sojournRepository.save(sojourn);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testGetSojourns() throws Exception {
        // Effectuer la requête GET et vérifier le résultat
        mockMvc.perform(get("/api/sojourns"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testCreateSojourn() throws Exception {
        // Préparez le DTO de test
        SojournDTO sojournDTO = new SojournDTO();
        sojournDTO.setEntryDate(LocalDate.now());
        sojournDTO.setExitDate(LocalDate.now().plusDays(1));
        sojournDTO.setSojournIdentifier("AB1234");
        sojournDTO.setPin(1234);
        sojournDTO.setClientId(existingClient.getId());
        sojournDTO.setRoomId(existingRoom.getId());
        sojournDTO.setStatus(RESERVED);

        // Récupérer le rôle existant
        String roleName = existingRoom.getRole().getName();

        // Effectuer la requête POST et vérifier le résultat
        mockMvc.perform(post("/api/sojourns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"entryDate\": \"" + sojournDTO.getEntryDate() + "\",\n" +
                                "  \"exitDate\": \"" + sojournDTO.getExitDate() + "\",\n" +
                                "  \"sojournIdentifier\": \"" + sojournDTO.getSojournIdentifier() + "\",\n" +
                                "  \"pin\":" + sojournDTO.getPin() + ",\n" +
                                "  \"clientId\": \"" + sojournDTO.getClientId() + "\",\n" +
                                "  \"roomId\": \"" + sojournDTO.getRoomId() + "\",\n" +
                                "  \"status\": \"" + sojournDTO.getStatus() + "\",\n" +
                                "  \"roomRole\": {\n" +
                                "    \"name\": \"" + roleName + "\"\n" +
                                "  }\n" +
                                "}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testUpdateSojourn() throws Exception {
        // Préparez les données de test
        SojournDTO sojournDTO = new SojournDTO();
        sojournDTO.setEntryDate(LocalDate.now());
        sojournDTO.setExitDate(LocalDate.now().plusDays(1));
        sojournDTO.setSojournIdentifier("AB1234");
        sojournDTO.setPin(1234);
        sojournDTO.setClientId(existingClient.getId());
        sojournDTO.setRoomId(existingRoom.getId());
        sojournDTO.setStatus(IN_PROGRESS);

        // Récupérer le rôle existant
        String roleName = existingRoom.getRole().getName();

        // Effectuer la requête PUT et vérifier le résultat
        mockMvc.perform(put("/api/sojourns/" + existingSojourn.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"entryDate\": \"" + sojournDTO.getEntryDate() + "\",\n" +
                                "  \"exitDate\": \"" + sojournDTO.getExitDate() + "\",\n" +
                                "  \"sojournIdentifier\": \"" + sojournDTO.getSojournIdentifier() + "\",\n" +
                                "  \"pin\":" + sojournDTO.getPin() + ",\n" +
                                "  \"clientId\": \"" + sojournDTO.getClientId() + "\",\n" +
                                "  \"roomId\": \"" + sojournDTO.getRoomId() + "\",\n" +
                                "  \"status\": \"" + sojournDTO.getStatus() + "\",\n" +
                                "  \"roomRole\": {\n" +
                                "    \"name\": \"" + roleName + "\"\n" +
                                "  }\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(IN_PROGRESS.name()));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testDeleteSojourn() throws Exception {
        // Préparez les données de test
        Sojourn sojourn = existingSojourn;

        // Effectuer la requête DELETE et vérifier le résultat
        mockMvc.perform(delete("/api/sojourns/" + sojourn.getId().toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testRecoverPasswordAndIdentifier() throws Exception {
        // Préparez les données de test
        Sojourn sojourn = existingSojourn;

        sojournRepository.save(sojourn);

        // Effectuer la requête GET pour récupérer le PIN et l'ID
        mockMvc.perform(get("/api/sojourns/" + sojourn.getId().toString() + "/recover"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testCancelSojourn() throws Exception {
        // Préparez les données de test
        Sojourn sojourn = existingSojourn;

        sojournRepository.save(sojourn);

        // Effectuer la requête PUT pour annuler le séjour
        mockMvc.perform(put("/api/sojourns/" + sojourn.getId().toString() + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(CANCELLED.name()));
    }

}