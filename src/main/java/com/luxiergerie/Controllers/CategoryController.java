package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.AccommodationDTO;
import com.luxiergerie.DTO.CategoryDTO;
import com.luxiergerie.Mapper.CategoryMapper;
import com.luxiergerie.Model.Entity.Category;
import com.luxiergerie.Repository.CategoryRepository;
import com.luxiergerie.Repository.SectionRepository;
import com.luxiergerie.Services.CategoryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;


@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final SectionRepository sectionRepository;
    private final CategoryService categoryService;

    public CategoryController(CategoryRepository categoryRepository, SectionRepository sectionRepository, CategoryService categoryService) {
        this.categoryRepository = categoryRepository;
        this.sectionRepository = sectionRepository;
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryMapper::MappedCategoryFrom)
                .collect(toList());
    }

    @GetMapping("/{id}")
    public CategoryDTO getCategoryById(@PathVariable UUID id) {
        try {
            return categoryService.getCategoryById(id);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}/accommodations")
    public List<AccommodationDTO> getAccommodationsByCategory(@PathVariable UUID id) {
        try {
            return categoryService.getAccommodationsByCategory(id);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/image/{category_id}")
    public ResponseEntity<byte[]> getCategoryImage(@PathVariable UUID category_id) {
        try {
            byte[] image = categoryService.getCategoryImage(category_id);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @PostMapping("/sections/{section_id}/categories")
    public ResponseEntity<CategoryDTO> createCategory(@RequestParam("name") String name,
                                                      @RequestParam("description") String description,
                                                      @PathVariable UUID section_id,
                                                      @RequestParam("image") MultipartFile image) {
        try {
            categoryService.createCategory(name, description, section_id, image);
            return new ResponseEntity<>(CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/sections/{section_id}/categories/{category_id}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestParam(value = "name", required = false) String name,
                                                      @RequestParam(value = "description", required = false) String description,
                                                      @PathVariable UUID section_id,
                                                      @PathVariable UUID category_id,
                                                      @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            categoryService.updateCategory(name, description, section_id, category_id, image);
            return new ResponseEntity<>(OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable UUID id) {
        categoryRepository.deleteById(id);
    }

    @GetMapping("/accommodations/{id}/category")
    public String getCategoryName(@PathVariable UUID id) {
        String categoryName = categoryRepository.getCategoryByAccommodation(id);
        return categoryName;
    }

}