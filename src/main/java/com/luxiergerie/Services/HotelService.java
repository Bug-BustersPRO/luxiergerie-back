package com.luxiergerie.Services;

import com.luxiergerie.DTO.HotelDTO;
import com.luxiergerie.Model.Entity.Hotel;
import com.luxiergerie.Repository.HotelRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.luxiergerie.Mapper.HotelMapper.MappedHotelFrom;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.*;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Transactional
    public ResponseEntity<byte[]> getHotelImage() {
        Optional<Hotel> hotelOptional = hotelRepository
                .findAll()
                .stream()
                .findFirst();
        if (hotelOptional.isPresent()) {
            byte[] image = hotelOptional.get().getImage();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        } else {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @Transactional
    public ResponseEntity<byte[]> getHotelBackgroundImage() {
        Optional<Hotel> hotelOptional = hotelRepository.findAll().stream().findFirst();
        if (hotelOptional.isPresent()) {
            byte[] backgroundImage = hotelOptional.get().getBackgroundImage();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(backgroundImage);
        } else {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @Transactional
    public ResponseEntity<HotelDTO> updateHotel(@PathVariable UUID id,
                                                @RequestParam(value = "name", required = false) String name,
                                                @RequestParam(value = "image", required = false) MultipartFile image,
                                                @RequestParam(value = "backgroundImage", required = false) MultipartFile backgroundImage,
                                                @RequestParam(value = "colors", required = false) List<String> colors) throws IOException {
        Optional<Hotel> hotelOptional = hotelRepository.findById(id);
        if (hotelOptional.isPresent()) {
            Hotel hotelToUpdate = hotelOptional.get();

            if (nonNull(name)) {
                hotelToUpdate.setName(name);
            }
            if (nonNull(colors)) {
                if (colors.size() > 3) {
                    return new ResponseEntity<>(BAD_REQUEST);
                }
                hotelToUpdate.setColors(colors);
            }
            if (nonNull(image) && !image.isEmpty()) {
                hotelToUpdate.setImage(image.getBytes());
            }
            if (nonNull(backgroundImage) && !backgroundImage.isEmpty()) {
                hotelToUpdate.setBackgroundImage(backgroundImage.getBytes());
            }

            Hotel updatedHotel = hotelRepository.save(hotelToUpdate);
            return new ResponseEntity<>(MappedHotelFrom(updatedHotel), OK);
        } else {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @Transactional
    public ResponseEntity<HotelDTO> createHotel(@RequestParam("name") String name,
                                                @RequestParam("image") MultipartFile image,
                                                @RequestParam("backgroundImage") MultipartFile backgroundImage,
                                                @RequestParam("colors") List<String> colors) throws IOException {

        List<String> imageExtension = List.of("image/jpeg", "image/png", "image/jpg", "image/gif");
        List<Hotel> hotels = hotelRepository.findAll();
        if (!hotels.isEmpty()) {
            return new ResponseEntity<>(CONFLICT);
        }

        if (colors.size() > 3) {
            return new ResponseEntity<>(BAD_REQUEST);
        }

        if (image.getSize() > 1_000_000 || !imageExtension.contains(image.getContentType())) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        if (backgroundImage.getSize() > 1_000_000 || !imageExtension.contains(backgroundImage.getContentType())) {
            return new ResponseEntity<>(BAD_REQUEST);
        }

        Hotel hotel = new Hotel();
        hotel.setName(name);
        hotel.setColors(colors);
        hotel.setImage(image.getBytes());
        hotel.setBackgroundImage(backgroundImage.getBytes());
        Hotel savedHotel = hotelRepository.save(hotel);
        return new ResponseEntity<>(MappedHotelFrom(savedHotel), CREATED);
    }

}