package com.luxiergerie.Services;

import com.luxiergerie.DTO.AccommodationDTO;
import com.luxiergerie.DTO.CategoryDTO;
import com.luxiergerie.Mapper.AccommodationMapper;
import com.luxiergerie.Model.Entity.Accommodation;
import com.luxiergerie.Model.Entity.Category;
import com.luxiergerie.Model.Entity.Section;
import com.luxiergerie.Repository.CategoryRepository;
import com.luxiergerie.Repository.SectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.luxiergerie.Mapper.CategoryMapper.MappedCategoryFrom;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SectionRepository sectionRepository;


    public CategoryService(CategoryRepository categoryRepository, SectionRepository sectionRepository) {
        this.categoryRepository = categoryRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public CategoryDTO getCategoryById(UUID id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            return MappedCategoryFrom(categoryOptional.get());
        } else {
            throw new RuntimeException("Category not found with ID: " + id);
        }
    }

    @Transactional
    public List<AccommodationDTO> getAccommodationsByCategory(UUID id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            List<Accommodation> accommodations = category.getAccommodations();
            return accommodations.stream()
                    .map(AccommodationMapper::MappedAccommodationFrom)
                    .collect(toList());
        } else {
            throw new RuntimeException("Category not found with id: " + id);
        }
    }

    @Transactional
    public byte[] getCategoryImage(UUID category_id) {
        Optional<Category> categoryOptional = categoryRepository.findById(category_id);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get().getImage();
        } else {
            throw new RuntimeException("Category not found with id: " + category_id);
        }
    }

    @Transactional
    public CategoryDTO createCategory(String name, String description, UUID section_id, MultipartFile image) throws IOException {
        List<String> imageExtension = List.of("image/jpeg", "image/png", "image/jpg", "image/gif");

        if (nonNull(image) && (image.getSize() > 1_000_000 || !imageExtension.contains(image.getContentType()))) {
            throw new IllegalArgumentException("Invalid image file");
        }

        Optional<Section> sectionOptional = sectionRepository.findById(section_id);
        if (sectionOptional.isPresent()) {
            Section section = sectionOptional.get();

            Category category = new Category();
            category.setName(name);
            category.setDescription(description);
            category.setImage(image.getBytes());
            category.setSection(section);

            Category savedCategory = categoryRepository.save(category);
            return MappedCategoryFrom(savedCategory);
        } else {
            throw new RuntimeException("Section not found with id: " + section_id);
        }
    }

    @Transactional
    public CategoryDTO updateCategory(String name,
                                      String description,
                                      UUID section_id,
                                      UUID category_id,
                                      MultipartFile image) throws IOException {
        List<String> imageExtension = List.of("image/jpeg", "image/png", "image/jpg", "image/gif");

        if (nonNull(image) && (image.getSize() > 1_000_000 || !imageExtension.contains(image.getContentType()))) {
            throw new IllegalArgumentException("Invalid image file");
        }

        UUID nonNullId = requireNonNull(category_id, "Category ID must not be null");
        UUID nonNullSectionId = requireNonNull(section_id, "Section ID must not be null");

        Optional<Category> categoryOptional = categoryRepository.findById(nonNullId);
        if (categoryOptional.isPresent()) {
            Category categoryToUpdate = categoryOptional.get();
            if (nonNull(name)) {
                categoryToUpdate.setName(name);
            }
            if (nonNull(description)) {
                categoryToUpdate.setDescription(description);
            }
            if (nonNull(image)) {
                categoryToUpdate.setImage(image.getBytes());
            }
            categoryToUpdate.setSection(sectionRepository.getReferenceById(section_id));
            Category updatedCategory = categoryRepository.save(categoryToUpdate);
            return MappedCategoryFrom(updatedCategory);
        } else {
            throw new RuntimeException("Category not found with id: " + nonNullId);
        }

    }

}