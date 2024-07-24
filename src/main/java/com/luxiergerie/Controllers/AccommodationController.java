package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.AccommodationDTO;
import com.luxiergerie.Domain.Entity.Accommodation;
import com.luxiergerie.Domain.Entity.Category;
import com.luxiergerie.Domain.Entity.Section;
import com.luxiergerie.Domain.Mapper.AccommodationMapper;
import com.luxiergerie.Domain.Repository.AccommodationRepository;
import com.luxiergerie.Domain.Repository.CategoryRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.luxiergerie.Domain.Mapper.AccommodationMapper.MappedAccommodationFrom;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api")
public class AccommodationController {

    private final AccommodationRepository accommodationRepository;
    private final CategoryRepository categoryRepository;

    public AccommodationController(AccommodationRepository accommodationRepository, CategoryRepository categoryRepository) {
        this.accommodationRepository = accommodationRepository;
        this.categoryRepository = categoryRepository;
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
        Optional<Accommodation> accommodationOptional = accommodationRepository.findById(accommodation_id).stream().findFirst();
        if (accommodationOptional.isPresent()) {
            byte[] image = accommodationOptional.get().getImage();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        } else {
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
        Optional<Category> categoryOptional = categoryRepository.findById(category_id);
        //List<String> imageExtension = List.of("image/jpeg", "image/png", "image/jpg", "image/gif");
        List<Accommodation> accommodations = accommodationRepository.findAll();
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();

            Accommodation accommodation = new Accommodation();
            accommodation.setName(name);
            accommodation.setDescription(description);
            accommodation.setPrice(price);
            accommodation.setReservable(isReservable);
            accommodation.setImage(image.getBytes());

            accommodation.setCategory(category);
            Accommodation savedAccomodation = accommodationRepository.save(accommodation);
            return new ResponseEntity<>(MappedAccommodationFrom(savedAccomodation), CREATED);
        }
        throw new RuntimeException("Section not found with id: " + category_id);
    }

    @PutMapping("/accommodations/{accommodation_id}/{category_id}")
    public ResponseEntity< AccommodationDTO> updateAccommodation(@RequestParam("name") String name,
                                                @PathVariable UUID accommodation_id,
                                                @PathVariable UUID category_id,
                                                @RequestParam(value = "description", required = false) String description,
                                                @RequestParam(value = "price") BigDecimal price,
                                                @RequestParam(value = "isReservable", required = false) boolean isReservable,
                                                @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        List<String> imageExtension = List.of("image/jpeg", "image/png", "image/jpg", "image/gif");

        if (image != null && (image.getSize() > 1_000_000 || !imageExtension.contains(image.getContentType()))) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        UUID nonNullId = requireNonNull(accommodation_id, "Accommodation ID must not be null");
        UUID nonNullSectionId = requireNonNull(category_id, "Category ID must not be null");

        Optional<Accommodation> accommodationOptional = accommodationRepository.findById(nonNullId);
        Optional<Category> categoryOptional = categoryRepository.findById(category_id);
        if (accommodationOptional.isPresent()) {
            Accommodation accommodationToUpdate = accommodationOptional.get();
            accommodationToUpdate.setName(name);
            accommodationToUpdate.setDescription(description);
            accommodationToUpdate.setPrice(price);
            if(image != null ) {
                accommodationToUpdate.setImage(image.getBytes());
            }
            accommodationToUpdate.setReservable(isReservable);
            accommodationToUpdate.setCategory(categoryRepository.getReferenceById(category_id));
            Accommodation updatedAccommodation = accommodationRepository.save(accommodationToUpdate);
            return new ResponseEntity<>(MappedAccommodationFrom(updatedAccommodation), OK);
        } else {
            throw new RuntimeException("Accommodation not found with id: " + nonNullId);
        }
    }

    @DeleteMapping("/accommodations/{id}")
    public void deleteAccommodation(@PathVariable("id") UUID id) {
        accommodationRepository.deleteById(id);
    }

}
