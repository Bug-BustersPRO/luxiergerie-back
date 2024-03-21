package com.luxiergerie.Controllers;

import com.luxiergerie.Domain.Entity.Accommodation;
import com.luxiergerie.Domain.Entity.Category;
import com.luxiergerie.Domain.Repository.AccommodationRepository;
import com.luxiergerie.Domain.Repository.CategoryRepository;

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
  public List<Accommodation> getAccommodations() {
    return accommodationRepository.findAll();
  }


  @GetMapping("/accommodations/{id}")
  public Accommodation getAccommodation(@PathVariable("id") UUID id) {
     UUID nonNullId = Objects.requireNonNull(id, "Accommodation ID must not be null");
    return this.accommodationRepository.findById(nonNullId)
        .orElseThrow(() -> new RuntimeException("Accommodation not found with id: " + nonNullId));
  }

   @PostMapping("/categories/{category_id}/accommodations")
  public Accommodation createAccommodation(@RequestBody Accommodation accommodation, @PathVariable UUID category_id) {
    Optional<Category> categoryOptional = categoryRepository.findById(category_id);
    if (categoryOptional.isPresent()) {
        Category category = categoryOptional.get();
        accommodation.setCategory(category);
        return accommodationRepository.save(accommodation);
    } else {
        throw new RuntimeException("Category not found with id: " + category_id);
    }
  }

  @PutMapping("/accommodations/{id}")
  public Accommodation updateAccommodation(@PathVariable("id") UUID id, @RequestBody Accommodation accommodation) {
    Optional<Accommodation> accommodationOptional = accommodationRepository.findById(id);
    if (accommodationOptional.isPresent()) {
        Accommodation accommodationToUpdate = accommodationOptional.get();
        accommodationToUpdate.setName(accommodation.getName());
        accommodationToUpdate.setDescription(accommodation.getDescription());
        accommodationToUpdate.setPrice(accommodation.getPrice());
        accommodationToUpdate.setImage(accommodation.getImage());
        return accommodationRepository.save(accommodationToUpdate);
    } else {
        throw new RuntimeException("Accommodation not found with id: " + id);
    }
  }

  @DeleteMapping("/accommodations/{id}")
  public void deleteAccommodation(@PathVariable("id") UUID id) {
    accommodationRepository.deleteById(id);
  }

}
