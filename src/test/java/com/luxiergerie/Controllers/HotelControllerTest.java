package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.HotelDTO;
import com.luxiergerie.Domain.Entity.Hotel;
import com.luxiergerie.Domain.Repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class HotelControllerTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelController hotelController;

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
    public void testUpdateHotel() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "image".getBytes());
        MockMultipartFile backgroundImage = new MockMultipartFile("backgroundImage", "backgroundImage.jpg", MediaType.IMAGE_JPEG_VALUE, "backgroundImage".getBytes());

        UUID hotelId = UUID.randomUUID();
        Hotel existingHotel = new Hotel(hotelId, "Hotel Name", image.getBytes(), List.of("Red", "Blue", "Green"), backgroundImage.getBytes());
        Hotel updatedHotel = new Hotel(hotelId, "Updated Hotel Name", image.getBytes(), List.of("White", "Yellow", "Green"), backgroundImage.getBytes());
        HotelDTO updatedHotelDto = new HotelDTO(hotelId, "Updated Hotel Name", image.getBytes(), List.of("White", "Yellow", "Green"), backgroundImage.getBytes());

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(existingHotel));
        when(hotelRepository.save(any(Hotel.class))).thenReturn(updatedHotel);

        ResponseEntity<HotelDTO> result = hotelController.updateHotel(hotelId, "Updated Hotel Name", image, List.of("White", "Yellow", "Green"));

        assertAll(
                () -> assertEquals(updatedHotelDto.getId(), result.getBody().getId()),
                () -> assertEquals(updatedHotelDto.getName(), result.getBody().getName()),
                () -> assertArrayEquals(updatedHotelDto.getImage(), result.getBody().getImage()),
                () -> assertEquals(updatedHotelDto.getColors(), result.getBody().getColors())
        );

        verify(hotelRepository, times(1)).findById(hotelId);
        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    public void testCreateHotel() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "image".getBytes());

        when(hotelRepository.save(any(Hotel.class))).thenReturn(new Hotel());

        mockMvc.perform(multipart("/api/hotel")
                        .file(image)
                        .param("name", "New Hotel")
                        .param("colors", "Red", "Blue", "Green"))
                .andExpect(status().isCreated());

        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

}
