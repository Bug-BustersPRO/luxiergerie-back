package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.PurchaseDTO;
import com.luxiergerie.Model.Entity.*;
import com.luxiergerie.Model.Enums.SojournStatus;
import com.luxiergerie.Repository.*;
import com.luxiergerie.Services.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PurchaseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SojournRepository sojournRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Client existingClient;
    private Purchase existingPurchase;
    private Accommodation accommodation;
    private Category category;
    private Sojourn sojourn;
    private SojournStatus sojournStatus;
    private Room room;

    @BeforeEach
    void setUp() {
        // Setup d'un client existant
        existingClient = new Client();
        UUID id = UUID.randomUUID();
        existingClient.setId(id);
        existingClient.setEmail("johndoe@example.com");
        existingClient.setFirstName("John");
        existingClient.setLastName("Doe");
        existingClient.setPhoneNumber("0123456789");
        existingClient.setPin(1234);
        existingClient = clientRepository.save(existingClient);

        // Setup d'une salle (room) existante
        room = new Room();
        room.setRoomNumber(101);
        room.setFloor(1);
        room.setClient(existingClient);
        room.setRole(roleRepository.findByName("ROLE_GOLD"));
        room = roomRepository.save(room);

        // Setup d'un sojourn existant
        var entyDate = LocalDateTime.now();
        var exitDate = LocalDateTime.now().plusDays(1);
        sojourn = new Sojourn();
        sojourn.setId(UUID.randomUUID());
        sojourn.setClient(existingClient);
        sojourn.setRoom(room);
        sojourn.setEntryDate(entyDate);
        sojourn.setExitDate(exitDate);
        sojourn.setStatus(sojournStatus);
        sojourn = sojournRepository.save(sojourn);

        room.setSojourns(new ArrayList<>(List.of(sojourn))); // Utilisez une ArrayList mutable ici
        roomRepository.save(room);

        // Setup et sauvegarde d'une category
        category = new Category();
        category.setId(UUID.randomUUID());
        category.setName("Boissons");
        category.setDescription("Boissons fraîches");
        category.setImage("dummyImage".getBytes());
        category = categoryRepository.save(category);

        // Setup d'une accommodation
        accommodation = new Accommodation();
        accommodation.setId(UUID.randomUUID());
        accommodation.setName("Coca-Cola");
        accommodation.setDescription("Boisson gazeuse");
        accommodation.setImage("dummyImage".getBytes());
        accommodation.setPrice(BigDecimal.valueOf(2.50));
        accommodation.setReservable(true);
        accommodation.setCategory(category); // Lien avec la catégorie sauvegardée
        accommodation.setQuantity(100);
        accommodation = accommodationRepository.save(accommodation);

        // Setup d'un achat existant
        existingPurchase = new Purchase();
        existingPurchase.setId(UUID.randomUUID());
        existingPurchase.setClient(existingClient);
        existingPurchase.setDate(new Date());
        existingPurchase.setStatus("En cours");
        existingPurchase.setTotalPrice(BigDecimal.valueOf(1000.00));
        existingPurchase.setAccommodations(new ArrayList<>(List.of(accommodation))); // Utilisez une ArrayList mutable ici
        existingPurchase = purchaseRepository.save(existingPurchase);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetPurchases() throws Exception {
        mockMvc.perform(get("/api/purchases"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetPurchase() throws Exception {
        mockMvc.perform(get("/api/purchases/" + existingPurchase.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.client.id").value(existingClient.getId().toString()));
    }

//    @Test
//    @WithMockUser(username = "testuser", roles = {"USER"})
//    void testCreatePurchase() throws Exception {
//        PurchaseDTO purchaseDTO = new PurchaseDTO();
//        purchaseDTO.setClient(existingClient);
//        purchaseDTO.setDate(new Date());
//        purchaseDTO.setStatus("Validée");
//
//        List<Accommodation> accommodations = new ArrayList<>();
//        accommodations.add(accommodation);
//        purchaseDTO.setAccommodations(accommodations);
//
//        // Conversion de la date en format ISO-8601
//        String formattedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(purchaseDTO.getDate());
//
//        mockMvc.perform(post("/api/purchases")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"client\":{\"id\":\"" + existingClient.getId() + "\"},"
//                                + "\"date\":\"" + formattedDate + "\","
//                                + "\"status\":\"Validée\","
//                                + "\"accommodations\":[{\"id\":\"" + accommodation.getId() + "\"}],"
//                                + "\"totalPrice\":100.00}")
//                ).andDo(print())
//                .andExpect(status().isCreated());
//    }

//    @Test
//    void testUpdatePurchase() throws Exception {
//        existingPurchase.setStatus("Validée");
//
//        mockMvc.perform(put("/api/purchases/" + existingPurchase.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"client\":{\"id\":\"" + existingClient.getId() + "\"},\"date\":\"" + new Date() + "\",\"status\":\"Validée\",\"totalPrice\":100.00}"))
//                .andExpect(status().isOk());
//    }

//    @Test
//    void testDeletePurchase() throws Exception {
//        mockMvc.perform(delete("/api/purchases/" + existingPurchase.getId()))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/api/purchases/" + existingPurchase.getId()))
//                .andExpect(status().isNotFound());
//    }
}
