package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.SectionDTO;
import com.luxiergerie.Mapper.SectionMapper;
import com.luxiergerie.Model.Entity.Category;
import com.luxiergerie.Model.Entity.Section;
import com.luxiergerie.Repository.SectionRepository;
import com.luxiergerie.Services.SectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.luxiergerie.Mapper.SectionMapper.MappedSectionFrom;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/sections")
public class SectionController {

    private final SectionRepository sectionRepository;
    private final SectionService sectionService;

    public SectionController(SectionRepository sectionRepository, SectionService sectionService) {
        this.sectionRepository = sectionRepository;
        this.sectionService = sectionService;
    }

    @GetMapping("")
    public List<SectionDTO> getSections() {
        List<Section> sections = sectionRepository.findAll();
        return sections.stream()
                .map(SectionMapper::MappedSectionFrom)
                .collect(toList());
    }

    @GetMapping("/{id}")
    public SectionDTO getSection(@PathVariable UUID id) {
        UUID nonNullId = requireNonNull(id, "Section ID must not be null");
        Section section = sectionRepository.findById(nonNullId)
                .orElseThrow(() -> new RuntimeException("Section not found with id: " + nonNullId));
        return MappedSectionFrom(section);
    }

    @GetMapping("/image/{section_id}")
    public ResponseEntity<byte[]> getSectionImage(@PathVariable("section_id") UUID sectionId) {
        try {
            byte[] image = sectionService.getSectionImage(sectionId);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @GetMapping("/{id}/categories")
    public List<Category> getCategoriesBySection(@PathVariable UUID id) {
        return sectionService.getCategoriesBySection(id);
    }

    @PostMapping("")
    public ResponseEntity<SectionDTO> createSection(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile image,
            @RequestParam("title") String title) {
        try {
            sectionService.createSection(name, description, image, title);
            return new ResponseEntity<>(CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SectionDTO> updateSection(
            @PathVariable UUID id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "title", required = false) String title) {
        try {
            sectionService.updateSection(id, name, description, image, title);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteSection(@PathVariable UUID id) {
        sectionRepository.deleteById(id);
    }

}