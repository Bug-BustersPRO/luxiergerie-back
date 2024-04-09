package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.AccommodationDTO;
import com.luxiergerie.DTO.CategoryDTO;
import com.luxiergerie.Domain.Entity.Accommodation;
import com.luxiergerie.Domain.Entity.Category;
import com.luxiergerie.Domain.Repository.AccommodationRepository;
import com.luxiergerie.Domain.Repository.CategoryRepository;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

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
        List<AccommodationDTO> accommodationDTOs = new ArrayList<>();
        for (Accommodation accommodation : accommodations) {
          accommodationDTOs.add(convertToDTO(accommodation));
        }
        return accommodationDTOs;
  }


  @GetMapping("/accommodations/{id}")
  public AccommodationDTO getAccommodation(@PathVariable("id") UUID id) {
    UUID nonNullId = requireNonNull(id, "Category ID must not be null");
    Accommodation accommodation = accommodationRepository.findById(nonNullId)
            .orElseThrow(() -> new RuntimeException("accommodation not found with id: " + nonNullId));
      return convertToDTO(accommodation);
  }

   @PostMapping("/categories/{category_id}/accommodations")
  public AccommodationDTO createAccommodation(@RequestBody AccommodationDTO accommodationDTO, @PathVariable UUID category_id) {
    Optional<Category> categoryOptional = categoryRepository.findById(category_id);
    if (categoryOptional.isPresent()) {
        Category category = categoryOptional.get();
        Accommodation accommodation = convertToEntity(accommodationDTO);
          accommodation.setCategory(category);
          Accommodation savedAccomodation = accommodationRepository.save(accommodation);
          return convertToDTO(savedAccomodation);
      }
      throw new RuntimeException("Section not found with id: " + category_id);
  }

  @PutMapping("/accommodations/{id}")
  public AccommodationDTO updateAccommodation(@PathVariable("id") UUID id, @RequestBody AccommodationDTO accommodationDTO) {
    UUID nonNullId = Objects.requireNonNull(id, "Accommodation ID must not be null");
        Optional<Accommodation> accommodationOptional = accommodationRepository.findById(nonNullId);
        if (accommodationOptional.isPresent()) {
            Accommodation accommodationToUpdate = accommodationOptional.get();
            accommodationToUpdate.setName(accommodationDTO.getName());
            accommodationToUpdate.setDescription(accommodationDTO.getDescription());
            accommodationToUpdate.setPrice(accommodationDTO.getPrice());
            accommodationToUpdate.setImage(accommodationDTO.getImage());
            Accommodation updatedAccommodation = accommodationRepository.save(accommodationToUpdate);
            return convertToDTO(updatedAccommodation);
        } else {
            throw new RuntimeException("Accommodation not found with id: " + nonNullId);
        }
  }

  @DeleteMapping("/accommodations/{id}")
  public void deleteAccommodation(@PathVariable("id") UUID id) {
    accommodationRepository.deleteById(id);
  }

  private AccommodationDTO convertToDTO(Accommodation accommodation) {
        AccommodationDTO accommodationDTO = new AccommodationDTO();
        accommodationDTO.setId(accommodation.getId());
        accommodationDTO.setName(accommodation.getName());
        accommodationDTO.setDescription(accommodation.getDescription());
        accommodationDTO.setPrice(accommodation.getPrice());
        accommodationDTO.setImage(accommodation.getImage());
        // Set CategoryDTO
        Category category = accommodation.getCategory();
        if (category != null) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(category.getId());
            categoryDTO.setName(category.getName());
            categoryDTO.setImage(category.getImage());
            accommodationDTO.setCategory(categoryDTO);
        }
        return accommodationDTO;
    }

    private Accommodation convertToEntity(AccommodationDTO accommodationDTO) {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(accommodationDTO.getId());
        accommodation.setName(accommodationDTO.getName());
        accommodation.setDescription(accommodationDTO.getDescription());
        accommodation.setPrice(accommodationDTO.getPrice());
        accommodation.setImage(accommodationDTO.getImage());
        return accommodation;
    }

}
