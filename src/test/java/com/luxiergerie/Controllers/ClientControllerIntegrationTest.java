//UNCOMMENT WHEN MYSQL IS CONFIGURED IN CI

//package com.luxiergerie.Controllers;
//
//import com.luxiergerie.DTO.ClientDTO;
//import com.luxiergerie.Model.Entity.Client;
//import com.luxiergerie.Model.Entity.Room;
//import com.luxiergerie.Model.Entity.Sojourn;
//import com.luxiergerie.Repository.ClientRepository;
//import com.luxiergerie.Repository.RoleRepository;
//import com.luxiergerie.Repository.RoomRepository;
//import com.luxiergerie.Repository.SojournRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.UUID;
//
//import static com.luxiergerie.Model.Enums.SojournStatus.RESERVED;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class ClientControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ClientRepository clientRepository;
//
//    @Autowired
//    private RoomRepository roomRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private SojournRepository sojournRepository;
//
//    private Client existingClient;
//    private Room existingRoom;
//    private Sojourn existingSojourn;
//
//    @BeforeEach
//    void setUp() {
//        // Préparez les données de test
//        Client client = new Client();
//        client.setId(UUID.randomUUID());
//        client.setFirstName("John");
//        client.setLastName("Doe");
//        client.setPhoneNumber("1234567890");
//        client.setEmail("test@email.fr");
//        client.setPin(1234);
//        existingClient = clientRepository.save(client);
//
//        Room room = new Room();
//        room.setId(UUID.randomUUID());
//        room.setRoomNumber(1);
//        room.setFloor(1);
//        room.setClient(existingClient);
//        room.setRole(roleRepository.findByName("ROLE_GOLD"));
//        existingRoom = roomRepository.save(room);
//
//        Sojourn sojourn = new Sojourn();
//        sojourn.setId(UUID.randomUUID());
//        sojourn.setClient(existingClient);
//        sojourn.setRoom(existingRoom);
//        sojourn.setEntryDate(LocalDate.now().minusDays(1).atTime(14, 0));
//        sojourn.setExitDate(LocalDate.now().plusDays(1).atTime(10, 0));
//        sojourn.setStatus(RESERVED);
//        sojourn.setSojournIdentifier("AB1234");
//        sojourn.setPin(1234);
//        existingSojourn = sojournRepository.save(sojourn);
//
//        room.setSojourns(List.of(existingSojourn));
//        existingRoom = roomRepository.save(room);
//
//    }
//
//    @Test
//    @WithMockUser(username = "testuser", roles = {"USER"})
//    public void testGetClients() throws Exception {
//        mockMvc.perform(get("/api/client"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(username = "testuser", roles = {"USER"})
//    public void testCreateClient() throws Exception {
//        ClientDTO clientDTO = new ClientDTO();
//        clientDTO.setFirstName("John");
//        clientDTO.setLastName("Doe");
//        clientDTO.setPhoneNumber("1234567890");
//        clientDTO.setEmail("fzzezef@mail.fr");
//        clientDTO.setRoom(null);
//        clientDTO.setSojourns(null);
//
//        mockMvc.perform(post("/api/client")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\n" +
//                                "    \"firstName\": \"John\",\n" +
//                                "    \"lastName\": \"Doe\",\n" +
//                                "    \"phoneNumber\": \"1234567890\",\n" +
//                                "    \"email\": \"fzzezef@mail.fr\",\n" +
//                                "    \"room\": null,\n" +
//                                "    \"sojourns\": null\n" +
//                                "}"))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    @WithMockUser(username = "testuser", roles = {"USER"})
//    public void testUpdateClient() throws Exception {
//        UUID clientId = existingClient.getId();
//        ClientDTO clientDTO = new ClientDTO();
//        clientDTO.setFirstName("John");
//        clientDTO.setLastName("Doe");
//        clientDTO.setPhoneNumber("1234567890");
//        clientDTO.setEmail("ddzazada@gmail.fr");
//        clientDTO.setRoom(null);
//        clientDTO.setSojourns(null);
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/client/{clientId}", clientId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\n" +
//                                "    \"firstName\": \"John\",\n" +
//                                "    \"lastName\": \"Doe\",\n" +
//                                "    \"phoneNumber\": \"1234567890\",\n" +
//                                "    \"email\": \" + clientDTO.getEmail() + \",\n" +
//                                "    \"room\": null,\n" +
//                                "    \"sojourns\": null\n" +
//                                "}"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(username = "testuser", roles = {"USER"})
//    public void testDeleteClient() throws Exception {
//        UUID clientId = existingClient.getId();
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/client/{clientId}", clientId))
//                .andExpect(status().is2xxSuccessful());
//    }
//
//}