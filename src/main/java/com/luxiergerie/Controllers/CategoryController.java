package com.luxiergerie.Controllers;

import com.luxiergerie.Domain.Entity.Accommodation;
import com.luxiergerie.Domain.Entity.Category;
import com.luxiergerie.Domain.Entity.Section;
import com.luxiergerie.Domain.Repository.CategoryRepository;
import com.luxiergerie.Domain.Repository.SectionRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

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
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  @GetMapping("/categories/{id}")
  public Category getCategoryById(@PathVariable UUID id) {
    UUID nonNullId = Objects.requireNonNull(id, "Category ID must not be null");
    return this.categoryRepository.findById(nonNullId)
        .orElseThrow(() -> new RuntimeException("Category not found with id: " + nonNullId));
  }

  @GetMapping("/categories/{id}/accommodations")
  public List<Accommodation> getAccommodationsByCategories(@PathVariable UUID id) {
    Optional<Category> categoryOptional = categoryRepository.findById(id);
    if (categoryOptional.isPresent()) {
        Category category = categoryOptional.get();
        return category.getAccommodations();
    } else {
        throw new RuntimeException("Section not found with id: " + id);
    }
  }

  @PostMapping("/sections/{section_id}/categories")
  public Category createCategory(@RequestBody Category category, @PathVariable UUID section_id) {
    Optional<Section> sectionOptional = sectionRepository.findById(section_id);
    if (sectionOptional.isPresent()) {
        Section section = sectionOptional.get();
        category.setSection(section);
        return categoryRepository.save(category);
    } else {
        throw new RuntimeException("Section not found with id: " + section_id);
    }
  }

  @PutMapping("/categories/{id}")
  public Category updateCategory(@PathVariable UUID id, @RequestBody Category category) {

      Optional<Category> categoryOptional = categoryRepository.findById(id);
    if (categoryOptional.isPresent()) {
        Category categoryToUpdate = categoryOptional.get();
        categoryToUpdate.setName(category.getName());
        categoryToUpdate.setImage(category.getImage());
        categoryToUpdate.setDescription(category.getDescription());
        return categoryRepository.save(categoryToUpdate);
    } else {
        throw new RuntimeException("Category not found with id: " + id);
    }
  }

  @DeleteMapping("/categories/{id}")
  public void deleteCategory(@PathVariable UUID id) {
    categoryRepository.deleteById(id);
  }
}
