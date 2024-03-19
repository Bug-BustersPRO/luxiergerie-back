package com.luxiergerie.Controllers;

import com.luxiergerie.Domain.Entity.Category;
import com.luxiergerie.Domain.Entity.Section;
import com.luxiergerie.Domain.Repository.SectionRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sections")
public class SectionController {

  private final SectionRepository sectionRepository;

  public SectionController(SectionRepository sectionRepository) {
    this.sectionRepository = sectionRepository;
  }

  @GetMapping("")
  public List<Section> getSections() {
    return sectionRepository.findAll();
  }

  @GetMapping("/{id}")
  public Section getSection(@PathVariable UUID id) {
  UUID nonNullId = Objects.requireNonNull(id, "Section ID must not be null");
  return this.sectionRepository.findById(nonNullId)
      .orElseThrow(() -> new RuntimeException("Section not found with id: " + nonNullId));
  }

  @GetMapping("/{id}/categories")
  public List<Category> getCategoriesBySection(@PathVariable UUID id) {
    Optional<Section> sectionOptional = sectionRepository.findById(id);
    if (sectionOptional.isPresent()) {
        Section section = sectionOptional.get();
        return section.getCategories();
    } else {
        throw new RuntimeException("Section not found with id: " + id);
    }
  }

  @PostMapping("")
  public Section createSection(@RequestBody Section section) {
    return sectionRepository.save(section);
  }

  @PutMapping("/{id}")
  public Section updateSection(@PathVariable UUID id, @RequestBody Section section) {
    Optional<Section> sectionOptional = sectionRepository.findById(id);
    if (sectionOptional.isPresent()) {
        Section sectionToUpdate = sectionOptional.get();
        sectionToUpdate.setName(section.getName());
        sectionToUpdate.setImage(section.getImage());
        return sectionRepository.save(sectionToUpdate);
    } else {
        throw new RuntimeException("Section not found with id: " + id);
    }
  }

  @DeleteMapping("/{id}")
  public void deleteSection(@PathVariable UUID id) {
    sectionRepository.deleteById(id);
  }

}
