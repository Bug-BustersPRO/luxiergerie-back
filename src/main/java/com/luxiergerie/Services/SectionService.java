package com.luxiergerie.Services;

import com.luxiergerie.DTO.SectionDTO;
import com.luxiergerie.Model.Entity.Category;
import com.luxiergerie.Model.Entity.Section;
import com.luxiergerie.Repository.SectionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.luxiergerie.Mapper.SectionMapper.MappedSectionFrom;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.*;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public byte[] getSectionImage(UUID sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Section not found with id: " + sectionId));
        return section.getImage();
    }

    @Transactional
    public List<Category> getCategoriesBySection(UUID sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Section not found with id: " + sectionId));
        return section.getCategories();
    }

    @Transactional
    public SectionDTO createSection(String name, String description, MultipartFile image, String title) throws IOException {
        List<String> imageExtension = List.of("image/jpeg", "image/png", "image/jpg", "image/gif");
        if (nonNull(image) && (image.getSize() > 1_000_000 || !imageExtension.contains(image.getContentType()))) {
            throw new IllegalArgumentException("Invalid image");
        }

        List<Section> sections = sectionRepository.findAll();
        Section section = new Section();
        section.setName(name);
        section.setDescription(description);
        section.setImage(image.getBytes());
        section.setTitle(title);

        Section savedSection = sectionRepository.save(section);
        return MappedSectionFrom(savedSection);
    }

    @Transactional
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
            if (nonNull(image)) {
                sectionToUpdate.setImage(image.getBytes());
            }
            Section updatedSection = sectionRepository.save(sectionToUpdate);
            return new ResponseEntity<>(MappedSectionFrom(updatedSection), OK);
        } else {
            throw new RuntimeException("Section not found with id: " + nonNullId);
        }
    }

}