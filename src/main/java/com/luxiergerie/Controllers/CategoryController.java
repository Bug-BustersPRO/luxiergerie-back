package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.AccommodationDTO;
import com.luxiergerie.DTO.CategoryDTO;
import com.luxiergerie.DTO.HotelDTO;
import com.luxiergerie.Domain.Entity.Accommodation;
import com.luxiergerie.Domain.Entity.Category;
import com.luxiergerie.Domain.Entity.Hotel;
import com.luxiergerie.Domain.Entity.Section;
import com.luxiergerie.Domain.Mapper.AccommodationMapper;
import com.luxiergerie.Domain.Mapper.CategoryMapper;
import com.luxiergerie.Domain.Repository.CategoryRepository;
import com.luxiergerie.Domain.Repository.SectionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import static com.luxiergerie.Domain.Mapper.HotelMapper.MappedHotelFrom;
import static java.util.Objects.requireNonNull;
import static com.luxiergerie.Domain.Mapper.CategoryMapper.MappedCategoryFrom;
import static java.util.stream.Collectors.*;
import static org.springframework.http.HttpStatus.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final SectionRepository sectionRepository;

    public CategoryController(CategoryRepository categoryRepository, SectionRepository sectionRepository) {
        this.categoryRepository = categoryRepository;
        this.sectionRepository = sectionRepository;
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
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if(categoryOptional.isPresent()) {
            return MappedCategoryFrom(categoryOptional.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with ID: " + id);
        }
    }

    @GetMapping("/{id}/accommodations")
    public List<AccommodationDTO> getAccommodationsByCategories(@PathVariable UUID id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            List<Accommodation> accommodations = category.getAccommodations();
            return accommodations.stream()
                    .map(AccommodationMapper::MappedAccommodationFrom)
                    .collect(toList());
        }
        throw new RuntimeException("Category not found with id: " + id);
    }

//    @PostMapping("/sections/{section_id}/categories")
//    public CategoryDTO createCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable UUID section_id, @RequestParam("image") MultipartFile image) throws IOException{
//      Optional<Section> sectionOptional = sectionRepository.findById(section_id);
//        List<String> imageExtension = List.of("image/jpeg", "image/png", "image/jpg", "image/gif");
//      if (sectionOptional.isPresent()) {
//          Section section = sectionOptional.get();
//
//
//          Category category = MappedCategoryFrom(categoryDTO);
//          category.setSection(section);
//          category.setImage(image.getBytes());
//         // category.setImageContentType(image.getContentType());
//          category.setImage(image.getBytes());
//          Category savedCategory = categoryRepository.save(category);
//          return MappedCategoryFrom(savedCategory);
//      }
//      throw new RuntimeException("Section not found with id: " + section_id);
//  }

    @GetMapping("/image/{category_id}")
    public ResponseEntity<byte[]> getCategoryImage(@PathVariable UUID category_id) {
        Optional<Category> categoryOptional = categoryRepository.findById(category_id).stream().findFirst();
        if (categoryOptional.isPresent()) {
            byte[] image = categoryOptional.get().getImage();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        } else {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @PostMapping("/sections/{section_id}/categories")
    public ResponseEntity<CategoryDTO> createCategory(@RequestParam("name") String name,
                                                        @RequestParam("description") String description,
                                                        @PathVariable UUID section_id,
                                                        @RequestParam("image") MultipartFile image) throws IOException {
        Optional<Section> sectionOptional = sectionRepository.findById(section_id);
        List<String> imageExtension = List.of("image/jpeg", "image/png", "image/jpg", "image/gif");

        if (image.getSize() > 1_000_000 || !imageExtension.contains(image.getContentType())) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        if (sectionOptional.isPresent()) {
            Section section = sectionOptional.get();

            Category category = new Category();
            category.setName(name);
            category.setDescription(description);
            category.setImage(image.getBytes());

            category.setSection(section);
            Category savedCategory = categoryRepository.save(category);
            return new ResponseEntity<>(MappedCategoryFrom(savedCategory), CREATED);
        }
        throw new RuntimeException("Category not found with id");
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
          Category updatedCategory = categoryRepository.save(categoryToUpdate);
          return MappedCategoryFrom(updatedCategory);
      }
      throw new RuntimeException("Category not found with id: " + nonNullId);
  }

    @DeleteMapping("/categories/{id}")
    public void deleteCategory(@PathVariable UUID id) {
        categoryRepository.deleteById(id);
    }

    @GetMapping("/accommodations/{id}/category")
    public String getCategoryName(@PathVariable UUID id) {
      String categoryName = categoryRepository.getCategoryByAccommodation(id);
      return categoryName;
    }


}
