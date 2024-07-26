package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.SectionDTO;
import com.luxiergerie.Domain.Entity.Category;
import com.luxiergerie.Domain.Entity.Section;
import com.luxiergerie.Domain.Repository.SectionRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.luxiergerie.Domain.Mapper.SectionMapper.MappedSectionFrom;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.*;

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

    @GetMapping("/image/{section_id}")
    public ResponseEntity<byte[]> getSectionImage(@PathVariable UUID section_id) {
        Optional<Section> sectionOptional = sectionRepository.findById(section_id).stream().findFirst();
        if (sectionOptional.isPresent()) {
            byte[] image = sectionOptional.get().getImage();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        } else {
            return new ResponseEntity<>(NOT_FOUND);
        }
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
  public ResponseEntity<SectionDTO> createSection(
          @RequestParam("name") String name,
          @RequestParam("description") String description,
          @RequestParam("image") MultipartFile image,
          @RequestParam("title") String title) throws IOException{
      List<String> imageExtension = List.of("image/jpeg", "image/png", "image/jpg", "image/gif");
      if (nonNull(image) && (image.getSize() > 1_000_000 || !imageExtension.contains(image.getContentType()))) {
          return new ResponseEntity<>(BAD_REQUEST);
      }
      List<Section> sections = sectionRepository.findAll();
      Section section = new Section();
      section.setName(name);
      section.setDescription(description);
      section.setImage(image.getBytes());
      section.setTitle(title);

    Section savedSection = sectionRepository.save(section);
    return new ResponseEntity<>(MappedSectionFrom(savedSection), CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<SectionDTO> updateSection(@PathVariable UUID id, @RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "description", required = false) String description,
                                  @RequestParam(value = "image", required = false) MultipartFile image,
                                  @RequestParam(value = "title", required = false) String title) throws IOException {
      List<String> imageExtension = List.of("image/jpeg", "image/png", "image/jpg", "image/gif");
      List<Section> sections = sectionRepository.findAll();
      if (nonNull(image) && (image.getSize() > 1_000_000 || !imageExtension.contains(image.getContentType()))) {
          return new ResponseEntity<>(BAD_REQUEST);
      }
    UUID nonNullId = Objects.requireNonNull(id, "Section ID must not be null");
    Optional<Section> sectionOptional = sectionRepository.findById(nonNullId);
    if (sectionOptional.isPresent()) {
        Section sectionToUpdate = sectionOptional.get();
        sectionToUpdate.setName(name);
        sectionToUpdate.setDescription(description);
        sectionToUpdate.setTitle(title);
        if(nonNull(image)) {
            sectionToUpdate.setImage(image.getBytes());
        }
        Section updatedSection = sectionRepository.save(sectionToUpdate);
        return new ResponseEntity<>(MappedSectionFrom(updatedSection), OK);
    } else {
        throw new RuntimeException("Section not found with id: " + nonNullId);
    }
}

  @DeleteMapping("/{id}")
  public void deleteSection(@PathVariable UUID id) {
    sectionRepository.deleteById(id);
  }

}
