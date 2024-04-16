package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.AccommodationDTO;
import com.luxiergerie.DTO.CategoryDTO;
import com.luxiergerie.DTO.SectionDTO;
import com.luxiergerie.Domain.Entity.Accommodation;
import com.luxiergerie.Domain.Entity.Category;
import com.luxiergerie.Domain.Entity.Section;
import com.luxiergerie.Domain.Repository.CategoryRepository;
import com.luxiergerie.Domain.Repository.SectionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final SectionRepository sectionRepository;

    public CategoryController(CategoryRepository categoryRepository, SectionRepository sectionRepository) {
        this.categoryRepository = categoryRepository;
        this.sectionRepository = sectionRepository;
    }

    @GetMapping("/categories")
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = new ArrayList<>();
        for (Category category : categories) {
          categoryDTOs.add(convertToDTO(category));
        }
        return categoryDTOs;
    }

    @GetMapping("/categories/{id}")
    public CategoryDTO getCategoryById(@PathVariable UUID id) {
        UUID nonNullId = requireNonNull(id, "Category ID must not be null");
        Category category = categoryRepository.findById(nonNullId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + nonNullId));
          return convertToDTO(category);
    }

    @GetMapping("/categories/{id}/accommodations")
    public List<AccommodationDTO> getAccommodationsByCategories(@PathVariable UUID id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            List<Accommodation> accommodations = category.getAccommodations();
            List<AccommodationDTO> accommodationDTOs = new ArrayList<>();
            for (Accommodation accommodation : accommodations) {
                AccommodationDTO accommodationDTO = convertToAccommodationDTO(accommodation);
                // Ajouter les informations de CategoryDTO à chaque AccommodationDTO
                CategoryDTO categoryDTO = convertToDTO(category);
                accommodationDTO.setCategory(categoryDTO);
                accommodationDTOs.add(accommodationDTO);
            }
            return accommodationDTOs;
        }
        throw new RuntimeException("Category not found with id: " + id);
    }

    @PostMapping("/sections/{section_id}/categories")
    public CategoryDTO createCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable UUID section_id) {
      Optional<Section> sectionOptional = sectionRepository.findById(section_id);
      if (sectionOptional.isPresent()) {
          Section section = sectionOptional.get();
          Category category = convertToEntity(categoryDTO);
          category.setSection(section);
          Category savedCategory = categoryRepository.save(category);
          return convertToDTO(savedCategory);
      }
      throw new RuntimeException("Section not found with id: " + section_id);
  }

    @PutMapping("/categories/{id}")
    public CategoryDTO updateCategory(@PathVariable UUID id, @RequestBody CategoryDTO categoryDTO) {
      UUID nonNullId = requireNonNull(id, "Category ID must not be null");
      Optional<Category> categoryOptional = categoryRepository.findById(nonNullId);
      if (categoryOptional.isPresent()) {
          Category categoryToUpdate = categoryOptional.get();
          categoryToUpdate.setName(categoryDTO.getName());
          categoryToUpdate.setImage(categoryDTO.getImage());
          categoryToUpdate.setDescription(categoryDTO.getDescription());
          categoryToUpdate.setAccommodations(categoryDTO.getAccommodations());
          categoryToUpdate.setSection(categoryDTO.getSection());
          Category updatedCategory = categoryRepository.save(categoryToUpdate);
          return convertToDTO(updatedCategory);
      }
      throw new RuntimeException("Category not found with id: " + nonNullId);
  }

    @DeleteMapping("/categories/{id}")
    public void deleteCategory(@PathVariable UUID id) {
        categoryRepository.deleteById(id);
    }

      // Méthode utilitaire pour convertir une entité Section en DTO
    private CategoryDTO convertToDTO(Category category) {
    CategoryDTO categoryDTO = new CategoryDTO();
    categoryDTO.setId(category.getId());
    categoryDTO.setName(category.getName());
    categoryDTO.setImage(category.getImage());
    categoryDTO.setAccommodations(category.getAccommodations());
    categoryDTO.setSection(category.getSection());
    return categoryDTO;
    }

// Méthode utilitaire pour convertir un DTO Section en entité
    private Category convertToEntity(CategoryDTO categoryDTO) {
    Category category = new Category();
    category.setId(categoryDTO.getId());
    category.setName(categoryDTO.getName());
    category.setImage(categoryDTO.getImage());
    category.setAccommodations(categoryDTO.getAccommodations());
    category.setSection(categoryDTO.getSection());
    return category;
    }

    private AccommodationDTO convertToAccommodationDTO(Accommodation accommodation) {
      AccommodationDTO accommodationDTO = new AccommodationDTO();
      accommodationDTO.setId(accommodation.getId());
      accommodationDTO.setName(accommodation.getName());
      accommodationDTO.setDescription(accommodation.getDescription());
      return accommodationDTO;
  }

}
