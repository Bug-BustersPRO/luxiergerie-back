package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.CategoryDTO;
import com.luxiergerie.Domain.Entity.Accommodation;
import com.luxiergerie.Domain.Entity.Category;
import com.luxiergerie.Domain.Entity.Section;
import com.luxiergerie.Domain.Repository.CategoryRepository;
import com.luxiergerie.Domain.Repository.SectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryControllerTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SectionRepository sectionRepository;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllCategoriesReturnsListOfCategories() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(new Category(), new Category()));
        assertEquals(2, categoryController.getAllCategories().size());
    }

    @Test
    public void getCategoryByIdReturnsCategoryWhenExists() {
        UUID id = UUID.randomUUID();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(new Category()));
        assertNotNull(categoryController.getCategoryById(id));
    }

    @Test
    public void getCategoryByIdThrowsExceptionWhenDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> categoryController.getCategoryById(id));
    }

    @Test
    public void getAccommodationsByCategoriesReturnsListOfAccommodationsWhenCategoryExists() {
        UUID id = UUID.randomUUID();
        Category category = new Category();
        category.setId(id);
        Accommodation accommodation1 = new Accommodation();
        accommodation1.setCategory(category);
        Accommodation accommodation2 = new Accommodation();
        accommodation2.setCategory(category);
        category.setAccommodations(Arrays.asList(accommodation1, accommodation2));
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        assertEquals(2, categoryController.getAccommodationsByCategories(id).size());
    }

    @Test
    public void getAccommodationsByCategoriesThrowsExceptionWhenCategoryDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> categoryController.getAccommodationsByCategories(id));
    }

    @Test
    public void createCategoryReturnsCategoryWhenSectionExists() {
        UUID id = UUID.randomUUID();
        when(sectionRepository.findById(id)).thenReturn(Optional.of(new Section()));
        assertNotNull(categoryController.createCategory(new CategoryDTO(), id));
    }

    @Test
    public void createCategoryThrowsExceptionWhenSectionDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(sectionRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> categoryController.createCategory(new CategoryDTO(), id));
    }

    @Test
    public void updateCategoryReturnsUpdatedCategoryWhenExists() {
        UUID id = UUID.randomUUID();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(new Category()));
        assertNotNull(categoryController.updateCategory(id, new CategoryDTO()));
    }

    @Test
    public void updateCategoryThrowsExceptionWhenDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> categoryController.updateCategory(id, new CategoryDTO()));
    }

    @Test
    public void deleteCategoryDoesNotThrowWhenExists() {
        UUID id = UUID.randomUUID();
        doNothing().when(categoryRepository).deleteById(id);
        assertDoesNotThrow(() -> categoryController.deleteCategory(id));
    }

}