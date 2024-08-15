package com.luxiergerie.Controllers;

import com.luxiergerie.Model.Entity.Hotel;
import com.luxiergerie.Repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Transactional
public class HotelControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HotelRepository hotelRepository;

    private Hotel existingHotel;

    @BeforeEach
    void setUp() {
        // Configuration d'un hôtel
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setColors(List.of("Red", "Blue", "Green"));
        hotel.setImage("dummyImage".getBytes());
        hotel.setBackgroundImage("dummyBackgroundImage".getBytes());
        existingHotel = hotelRepository.save(hotel);
    }

    @Test
    void testGetHotel() throws Exception {
        mockMvc.perform(get("/api/hotel/infos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Hotel"))
                .andExpect(jsonPath("$[0].colors[0]").value("Red"))
                .andExpect(jsonPath("$[0].colors[1]").value("Blue"))
                .andExpect(jsonPath("$[0].colors[2]").value("Green"))
                .andExpect(jsonPath("$[0].image").exists())
                .andExpect(jsonPath("$[0].backgroundImage").exists());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetHotelImage() throws Exception {
        mockMvc.perform(get("/api/hotel/image"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(existingHotel.getImage()));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetHotelBackgroundImage() throws Exception {
        mockMvc.perform(get("/api/hotel/background-image"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(existingHotel.getBackgroundImage()));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testUpdateHotel() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "updatedImage".getBytes());
        MockMultipartFile backgroundImage = new MockMultipartFile("backgroundImage", "backgroundImage.jpg", "image/jpeg", "updatedBackgroundImage".getBytes());

        mockMvc.perform(multipart("/api/hotel/" + existingHotel.getId())
                        .file(image)
                        .file(backgroundImage)
                        .param("name", "Updated Hotel Name")
                        .param("colors", "White,Yellow,Green")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk());

        Optional<Hotel> updatedHotel = hotelRepository.findById(existingHotel.getId());
        assertTrue(updatedHotel.isPresent());
        assertEquals("Updated Hotel Name", updatedHotel.get().getName());
        assertEquals("White", updatedHotel.get().getColors().get(0));
        assertArrayEquals("updatedImage".getBytes(), updatedHotel.get().getImage());
        assertArrayEquals("updatedBackgroundImage".getBytes(), updatedHotel.get().getBackgroundImage());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCreateHotel() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "newImage".getBytes());
        MockMultipartFile backgroundImage = new MockMultipartFile("backgroundImage", "backgroundImage.jpg", "image/jpeg", "newBackgroundImage".getBytes());

        mockMvc.perform(multipart("/api/hotel")
                        .file(image)
                        .file(backgroundImage)
                        .param("name", "New Hotel")
                        .param("colors", "Black,White,Blue"))
                .andExpect(status().isCreated());
    }

}