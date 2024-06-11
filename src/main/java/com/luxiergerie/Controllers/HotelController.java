package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.HotelDTO;
import com.luxiergerie.Domain.Entity.Hotel;
import com.luxiergerie.Domain.Mapper.HotelMapper;
import com.luxiergerie.Domain.Repository.HotelRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static com.luxiergerie.Domain.Mapper.HotelMapper.MappedHotelFrom;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/hotel")
public class HotelController {

    private final HotelRepository hotelRepository;

    public HotelController(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @GetMapping
    public List<HotelDTO> getHotel() {
        List<Hotel> hotels = hotelRepository.findAll();
        return hotels.stream()
                .map(HotelMapper::MappedHotelFrom)
                .collect(toList());
    }

    @GetMapping("/image")
    public ResponseEntity<byte[]> getHotelImage() {
        Optional<Hotel> hotelOptional = hotelRepository.findAll().stream().findFirst();
        if (hotelOptional.isPresent()) {
            byte[] image = hotelOptional.get().getImage();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelDTO> updateHotel(@PathVariable UUID id,
                                                @RequestParam(value = "name", required = false) String name,
                                                @RequestParam(value = "image", required = false) MultipartFile image,
                                                @RequestParam(value = "colors", required = false) List<String> colors) throws IOException {
        Optional<Hotel> hotelOptional = hotelRepository.findById(id);
        if (hotelOptional.isPresent()) {
            Hotel hotelToUpdate = hotelOptional.get();

            if (name != null) {
                hotelToUpdate.setName(name);
            }
            if (colors != null) {
                if (colors.size() > 3) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                hotelToUpdate.setColors(colors);
            }
            if (image != null && !image.isEmpty()) {
                hotelToUpdate.setImage(image.getBytes());
            }

            Hotel updatedHotel = hotelRepository.save(hotelToUpdate);
            return new ResponseEntity<>(MappedHotelFrom(updatedHotel), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<HotelDTO> createHotel(@RequestParam("name") String name,
                                                @RequestParam("image") MultipartFile image,
                                                @RequestParam("colors") List<String> colors) throws IOException {
        List<Hotel> hotels = hotelRepository.findAll();
        if (!hotels.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (colors.size() > 3) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Hotel hotel = new Hotel();
        hotel.setName(name);
        hotel.setColors(colors);
        hotel.setImage(image.getBytes());
        Hotel savedHotel = hotelRepository.save(hotel);
        return new ResponseEntity<>(MappedHotelFrom(savedHotel), HttpStatus.CREATED);
    }

}
