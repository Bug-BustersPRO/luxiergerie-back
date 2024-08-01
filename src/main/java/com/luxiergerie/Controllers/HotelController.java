package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.HotelDTO;
import com.luxiergerie.Mapper.HotelMapper;
import com.luxiergerie.Model.Entity.Hotel;
import com.luxiergerie.Repository.HotelRepository;
import com.luxiergerie.Services.HotelService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/hotel")
public class HotelController {

    private final HotelRepository hotelRepository;
    private final HotelService hotelService;

    public HotelController(HotelRepository hotelRepository, HotelService hotelService) {
        this.hotelRepository = hotelRepository;
        this.hotelService = hotelService;
    }

    @GetMapping("/infos")
    public List<HotelDTO> getHotel() {
        List<Hotel> hotels = hotelRepository.findAll();
        return hotels.stream()
                .map(HotelMapper::MappedHotelFrom)
                .collect(toList());
    }

    @GetMapping("/image")
    public ResponseEntity<byte[]> getHotelImage() {
        try {
            byte[] image = hotelService.getHotelImage().getBody();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @GetMapping("/background-image")
    public ResponseEntity<byte[]> getHotelBackgroundImage() {
        try {
            byte[] backgroundImage = hotelService.getHotelBackgroundImage().getBody();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(backgroundImage);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelDTO> updateHotel(@PathVariable UUID id,
                                                @RequestParam(value = "name", required = false) String name,
                                                @RequestParam(value = "image", required = false) MultipartFile image,
                                                @RequestParam(value = "backgroundImage", required = false) MultipartFile backgroundImage,
                                                @RequestParam(value = "colors", required = false) List<String> colors) throws IOException {
        try {
            hotelService.updateHotel(id, name, image, backgroundImage, colors);
            return new ResponseEntity<>(OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<HotelDTO> createHotel(@RequestParam("name") String name,
                                                @RequestParam("image") MultipartFile image,
                                                @RequestParam("backgroundImage") MultipartFile backgroundImage,
                                                @RequestParam("colors") List<String> colors) throws IOException {
        try {
            hotelService.createHotel(name, image, backgroundImage, colors);
            return new ResponseEntity<>(CREATED);

        } catch (BadRequestException e) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

}