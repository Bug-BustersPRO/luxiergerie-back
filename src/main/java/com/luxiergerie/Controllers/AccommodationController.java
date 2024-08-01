package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.AccommodationDTO;
import com.luxiergerie.Mapper.AccommodationMapper;
import com.luxiergerie.Model.Entity.Accommodation;
import com.luxiergerie.Repository.AccommodationRepository;
import com.luxiergerie.Repository.CategoryRepository;
import com.luxiergerie.Services.AccommodationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.luxiergerie.Mapper.AccommodationMapper.MappedAccommodationFrom;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api")
public class AccommodationController {

    private final AccommodationRepository accommodationRepository;
    private final CategoryRepository categoryRepository;

    private final AccommodationService accommodationService;

    public AccommodationController(AccommodationRepository accommodationRepository, CategoryRepository categoryRepository, AccommodationService accommodationService) {
        this.accommodationRepository = accommodationRepository;
        this.categoryRepository = categoryRepository;
        this.accommodationService = accommodationService;
    }

    @GetMapping("/accommodations")
    public List<AccommodationDTO> getAccommodations() {
        List<Accommodation> accommodations = accommodationRepository.findAll();
        return accommodations.stream()
                .map(AccommodationMapper::MappedAccommodationFrom)
                .collect(toList());
    }

    @GetMapping("/accommodations/{id}")
    public AccommodationDTO getAccommodation(@PathVariable("id") UUID id) {
        UUID nonNullId = requireNonNull(id, "Category ID must not be null");
        Accommodation accommodation = accommodationRepository.findById(nonNullId)
                .orElseThrow(() -> new RuntimeException("accommodation not found with id: " + nonNullId));
        return MappedAccommodationFrom(accommodation);
    }

    @GetMapping("/accommodations/image/{accommodation_id}")
    public ResponseEntity<byte[]> getAccommodationImage(@PathVariable UUID accommodation_id) {
        try {
            byte[] image = accommodationService.getAccommodationImage(accommodation_id);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @PostMapping("/categories/{category_id}/accommodations")
    public ResponseEntity<AccommodationDTO> createAccommodation(@RequestParam("name") String name,
                                                                @PathVariable UUID category_id,
                                                                @RequestParam("description") String description,
                                                                @RequestParam("price") BigDecimal price,
                                                                @RequestParam("isReservable") boolean isReservable,
                                                                @RequestParam("image") MultipartFile image) throws IOException {
        try {
            accommodationService.createAccommodation(name, category_id, description, price, isReservable, image);
            return new ResponseEntity<>(CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/accommodations/{accommodation_id}/{category_id}")
    public ResponseEntity<AccommodationDTO> updateAccommodation(@RequestParam("name") String name,
                                                                @PathVariable UUID accommodation_id,
                                                                @PathVariable UUID category_id,
                                                                @RequestParam(value = "description", required = false) String description,
                                                                @RequestParam(value = "price") BigDecimal price,
                                                                @RequestParam(value = "isReservable", required = false) boolean isReservable,
                                                                @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            accommodationService.updateAccommodation(accommodation_id, category_id, name, description, price, isReservable, image);
            return new ResponseEntity<>(OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/accommodations/{id}")
    public void deleteAccommodation(@PathVariable("id") UUID id) {
        accommodationRepository.deleteById(id);
    }

}
