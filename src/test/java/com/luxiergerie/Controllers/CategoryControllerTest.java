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
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;

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
    public void getAllCategories() {
        when(categoryRepository.findAll()).thenReturn
                (Arrays.asList(new Category( UUID.randomUUID(), "name", "description", "image", new ArrayList<>(), new Section()),
                        new Category( UUID.randomUUID(), "name", "description", "image", new ArrayList<>(), new Section())));
        assertEquals(2, categoryController.getAllCategories().size());
    }

    @Test
    public void getCategoryById() {
        UUID id = UUID.randomUUID();
        when(categoryRepository.findById(id))
                .thenReturn(Optional.of(new Category(id, "name", "description", "image", new ArrayList<>(), new Section())));
        assertNotNull(categoryController.getCategoryById(id));
    }

    @Test
    public void getCategoryByIdThrowsExceptionWhenDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> categoryController.getCategoryById(id));
    }

    @Test
    public void getAccommodationsByCategories() {
        UUID id = UUID.randomUUID();
        Category category = new Category( UUID.randomUUID(), "name", "description", "image", new ArrayList<>(), new Section());
        category.setId(id);
        Accommodation accommodation1 = new Accommodation(UUID.randomUUID(), "name", "description", "image", BigDecimal.valueOf(10.00), category, new ArrayList<>());
        accommodation1.setCategory(category);
        Accommodation accommodation2 = new Accommodation( UUID.randomUUID(), "name", "description", "image", BigDecimal.valueOf(10.00), category, new ArrayList<>());
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
    public void createCategory() {
        UUID id = UUID.randomUUID();
        Section section = new Section(id, "name", "description", "image", "title", new ArrayList<>());
        when(sectionRepository.findById(id)).thenReturn(Optional.of(section));
        Category category = new Category(UUID.randomUUID(), "name", "description", "image", new ArrayList<>(), section);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDTO result = categoryController.createCategory(new CategoryDTO(), id);
        assertEquals(category.getId(), result.getId());
    }

    @Test
    public void createCategoryThrowsExceptionWhenSectionDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(sectionRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> categoryController.createCategory(new CategoryDTO(), id));
    }

    @Test
    public void updateCategory() {
        UUID id = UUID.randomUUID();
        Accommodation accommodation = new Accommodation(UUID.randomUUID(), "name", "description", "image", BigDecimal.valueOf(10.00), new Category(), new ArrayList<>());
        Category existingCategory = new Category(id, "name", "description", "image", Collections.singletonList(accommodation), new Section());
        Category updatedCategory = new Category(id, "new name", "new description", "new image", Collections.singletonList(accommodation), new Section());

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        CategoryDTO updatedCategoryDTO = new CategoryDTO();
        updatedCategoryDTO.setId(updatedCategory.getId());
        updatedCategoryDTO.setName(updatedCategory.getName());
        updatedCategoryDTO.setDescription(updatedCategory.getDescription());
        updatedCategoryDTO.setImage(updatedCategory.getImage());
        updatedCategoryDTO.setAccommodations(updatedCategory.getAccommodations());
        updatedCategoryDTO.setSection(updatedCategory.getSection());

        CategoryDTO result = categoryController.updateCategory(id, updatedCategoryDTO);
        assertEquals(updatedCategory.getId(), result.getId());
        verify(categoryRepository).save(any(Category.class));
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