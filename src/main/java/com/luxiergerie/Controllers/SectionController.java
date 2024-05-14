package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.SectionDTO;
import com.luxiergerie.Domain.Entity.Category;
import com.luxiergerie.Domain.Entity.Section;
import com.luxiergerie.Domain.Repository.SectionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;
import static com.luxiergerie.Domain.Mapper.SectionMapper.MappedSectionFrom;

@RestController
@RequestMapping("/api/sections")
public class SectionController {

  private final SectionRepository sectionRepository;

  public SectionController(SectionRepository sectionRepository) {
    this.sectionRepository = sectionRepository;
  }

  @GetMapping("")
  public List<SectionDTO> getSections() {
        List<Section> sections = sectionRepository.findAll();
        List<SectionDTO> sectionDTOs = new ArrayList<>();
        for (Section section : sections) {
            sectionDTOs.add(MappedSectionFrom(section));
        }
        return sectionDTOs;
      }

  @GetMapping("/{id}")
   public SectionDTO getSection(@PathVariable UUID id) {
        UUID nonNullId = Objects.requireNonNull(id, "Section ID must not be null");
        Section section = sectionRepository.findById(nonNullId)
                .orElseThrow(() -> new RuntimeException("Section not found with id: " + nonNullId));
        return MappedSectionFrom(section);
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
  public SectionDTO createSection(@RequestBody SectionDTO sectionDTO) {
    Section section = MappedSectionFrom(sectionDTO);
    Section savedSection = sectionRepository.save(section);
    return MappedSectionFrom(savedSection);
  }

  @PutMapping("/{id}")
  public SectionDTO updateSection(@PathVariable UUID id, @RequestBody SectionDTO sectionDTO) {
    UUID nonNullId = Objects.requireNonNull(id, "Section ID must not be null");
    Optional<Section> sectionOptional = sectionRepository.findById(nonNullId);
    if (sectionOptional.isPresent()) {
        Section sectionToUpdate = sectionOptional.get();
        sectionToUpdate.setName(sectionDTO.getName());
        sectionToUpdate.setImage(sectionDTO.getImage());
        Section updatedSection = sectionRepository.save(sectionToUpdate);
        return MappedSectionFrom(updatedSection);
    } else {
        throw new RuntimeException("Section not found with id: " + nonNullId);
    }
}

  @DeleteMapping("/{id}")
  public void deleteSection(@PathVariable UUID id) {
    sectionRepository.deleteById(id);
  }

}
