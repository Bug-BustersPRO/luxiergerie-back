package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.HotelDTO;
import com.luxiergerie.Domain.Entity.Hotel;
import com.luxiergerie.Domain.Mapper.HotelMapper;
import com.luxiergerie.Domain.Repository.HotelRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/hotel")
public class HotelController {

    private final HotelRepository hotelRepository;

    public HotelController(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @GetMapping
    public List<HotelDTO> getAllHotels() {
        List<Hotel> hotels = hotelRepository.findAll();
        return hotels.stream()
                .map(HotelMapper::MappedHotelFrom)
                .collect(toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDTO> getHotelById(@PathVariable UUID id) {
        Optional<Hotel> hotelOptional = hotelRepository.findById(id);
        if (hotelOptional.isPresent()) {
            return new ResponseEntity<>(HotelMapper.MappedHotelFrom(hotelOptional.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<HotelDTO> createHotel(@RequestParam("name") String name,
                                                @RequestParam("image") MultipartFile image,
                                                @RequestParam("colors")  List<String> colors) throws IOException {
        Hotel hotel = new Hotel();
        hotel.setName(name);
        hotel.setColors(colors);
        hotel.setImage(image.getBytes());
        Hotel savedHotel = hotelRepository.save(hotel);
        return new ResponseEntity<>(HotelMapper.MappedHotelFrom(savedHotel), HttpStatus.CREATED);
    }
}
