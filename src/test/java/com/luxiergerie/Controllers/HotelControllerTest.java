package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.HotelDTO;
import com.luxiergerie.Model.Entity.Hotel;
import com.luxiergerie.Repository.HotelRepository;
import com.luxiergerie.Services.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HotelControllerTest {

    @Mock
    private HotelRepository hotelRepository;
    @InjectMocks
    private HotelController hotelController;
    @Mock
    private HotelService hotelService;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(hotelController).build();
    }

    @Test
    public void testGetHotel() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "image".getBytes());

        Hotel hotel = new Hotel();
        hotel.setImage(image.getBytes());
        hotel.setName("Hotel Name");
        hotel.setColors(List.of("Red", "Blue", "Green"));

        when(hotelRepository.findAll()).thenReturn(Collections.singletonList(hotel));

        mockMvc.perform(get("/api/hotel/infos"))
                .andExpect(status().isOk());

        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateHotel_Success() throws IOException {
        // Arrange
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "image".getBytes());
        MockMultipartFile backgroundImage = new MockMultipartFile("backgroundImage", "backgroundImage.jpg", "image/jpeg", "backgroundImage".getBytes());

        UUID hotelId = randomUUID();
        Hotel existingHotel = new Hotel(hotelId, "Hotel Name", image.getBytes(), List.of("Red", "Blue", "Green"), backgroundImage.getBytes());
        HotelDTO updatedHotelDto = new HotelDTO(hotelId, "Updated Hotel Name", image.getBytes(), List.of("White", "Yellow", "Green"), backgroundImage.getBytes());

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(existingHotel));
        when(hotelService.updateHotel(eq(hotelId), eq("Updated Hotel Name"), eq(image), eq(backgroundImage), eq(List.of("White", "Yellow", "Green"))))
                .thenReturn(new ResponseEntity<>(updatedHotelDto, OK));

        // Act
        ResponseEntity<HotelDTO> response = hotelController.updateHotel(hotelId, "Updated Hotel Name", image, backgroundImage, List.of("White", "Yellow", "Green"));

        // Assert
        assertEquals(OK, response.getStatusCode());

    }

    @Test
    public void testCreateHotel() throws Exception {
        // Arrange
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "image".getBytes());
        MockMultipartFile backgroundImage = new MockMultipartFile("backgroundImage", "backgroundImage.jpg", "image/jpeg", "backgroundImage".getBytes());

        HotelDTO hotelDTO = new HotelDTO(null, "New Hotel", image.getBytes(), List.of("Red", "Blue", "Green"), backgroundImage.getBytes());
        Hotel expectedHotel = new Hotel(randomUUID(), hotelDTO.getName(), hotelDTO.getImage(), hotelDTO.getColors(), hotelDTO.getBackgroundImage());

        when(hotelService.createHotel("Name", backgroundImage, backgroundImage, List.of("Red", "Blue", "Green"))).thenReturn(new ResponseEntity<>(hotelDTO, CREATED));
        when(hotelRepository.save(any(Hotel.class))).thenReturn(expectedHotel);

        // Act
        ResponseEntity<HotelDTO> response = hotelController.createHotel(hotelDTO.getName(), image, backgroundImage, hotelDTO.getColors());

        // Assert
        assertNotNull(response);
        assertEquals(CREATED, response.getStatusCode());
    }

}