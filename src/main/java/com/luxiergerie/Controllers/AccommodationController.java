package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.AccommodationDTO;
import com.luxiergerie.Domain.Entity.Accommodation;
import com.luxiergerie.Domain.Entity.Category;
import com.luxiergerie.Domain.Mapper.AccommodationMapper;
import com.luxiergerie.Domain.Repository.AccommodationRepository;
import com.luxiergerie.Domain.Repository.CategoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.luxiergerie.Domain.Mapper.AccommodationMapper.MappedAccommodationFrom;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

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

    @PostMapping("/categories/{category_id}/accommodations")
    public AccommodationDTO createAccommodation(@RequestBody AccommodationDTO accommodationDTO, @PathVariable UUID category_id) {
        Optional<Category> categoryOptional = categoryRepository.findById(category_id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            Accommodation accommodation = MappedAccommodationFrom(accommodationDTO);
            accommodation.setCategory(category);
            Accommodation savedAccomodation = accommodationRepository.save(accommodation);
            return MappedAccommodationFrom(savedAccomodation);
        }
        throw new RuntimeException("Section not found with id: " + category_id);
    }

    @PutMapping("/accommodations/{id}")
    public AccommodationDTO updateAccommodation(@PathVariable("id") UUID id, @RequestBody AccommodationDTO accommodationDTO) {
        UUID nonNullId = requireNonNull(id, "Accommodation ID must not be null");
        Optional<Accommodation> accommodationOptional = accommodationRepository.findById(nonNullId);
        if (accommodationOptional.isPresent()) {
            Accommodation accommodationToUpdate = accommodationOptional.get();
            accommodationToUpdate.setName(accommodationDTO.getName());
            accommodationToUpdate.setDescription(accommodationDTO.getDescription());
            accommodationToUpdate.setPrice(accommodationDTO.getPrice());
            accommodationToUpdate.setImage(accommodationDTO.getImage());
            accommodationToUpdate.setReservable(accommodationDTO.isReservable());
            accommodationToUpdate.setQuantity(accommodationDTO.getQuantity());
            Accommodation updatedAccommodation = accommodationRepository.save(accommodationToUpdate);
            return MappedAccommodationFrom(updatedAccommodation);
        } else {
            throw new RuntimeException("Accommodation not found with id: " + nonNullId);
        }
    }

    @DeleteMapping("/accommodations/{id}")
    public void deleteAccommodation(@PathVariable("id") UUID id) {
        accommodationRepository.deleteById(id);
    }

}
