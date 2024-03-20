package com.luxiergerie.Controllers;

import com.luxiergerie.Domain.Entity.Accomodation;
import com.luxiergerie.Domain.Entity.Category;
import com.luxiergerie.Domain.Repository.AccomodationRepository;
import com.luxiergerie.Domain.Repository.CategoryRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

@RestController
public class AccomodationController {

  private final AccomodationRepository accomodationRepository;
  private final CategoryRepository categoryRepository;

  public AccomodationController(AccomodationRepository accomodationRepository, CategoryRepository categoryRepository) {
    this.accomodationRepository = accomodationRepository;
    this.categoryRepository = categoryRepository;
  }

  @GetMapping("/accomodations")
  public List<Accomodation> getAccomodations() {
    return accomodationRepository.findAll();
  }


  @GetMapping("/accomodations/{id}")
  public Accomodation getAccomodation(@PathVariable("id") UUID id) {
     UUID nonNullId = Objects.requireNonNull(id, "Accomodation ID must not be null");
    return this.accomodationRepository.findById(nonNullId)
        .orElseThrow(() -> new RuntimeException("Accomodation not found with id: " + nonNullId));
  }

   @PostMapping("/categories/{category_id}/accomodations")
  public Accomodation createAccomodation(@RequestBody Accomodation accomodation, @PathVariable UUID category_id) {
    Optional<Category> categoryOptional = categoryRepository.findById(category_id);
    if (categoryOptional.isPresent()) {
        Category category = categoryOptional.get();
        accomodation.setCategory(category);
        return accomodationRepository.save(accomodation);
    } else {
        throw new RuntimeException("Category not found with id: " + category_id);
    }
  }

  @PutMapping("/accomodations/{id}")
  public Accomodation updateAccomodation(@PathVariable("id") UUID id, @RequestBody Accomodation accomodation) {
    Optional<Accomodation> accomodationOptional = accomodationRepository.findById(id);
    if (accomodationOptional.isPresent()) {
        Accomodation accomodationToUpdate = accomodationOptional.get();
        accomodationToUpdate.setName(accomodation.getName());
        accomodationToUpdate.setDescription(accomodation.getDescription());
        accomodationToUpdate.setPrice(accomodation.getPrice());
        accomodationToUpdate.setImage(accomodation.getImage());
        return accomodationRepository.save(accomodationToUpdate);
    } else {
        throw new RuntimeException("Accomodation not found with id: " + id);
    }
  }

  @DeleteMapping("/accomodations/{id}")
  public void deleteAccomodation(@PathVariable("id") UUID id) {
    accomodationRepository.deleteById(id);
  }

}
