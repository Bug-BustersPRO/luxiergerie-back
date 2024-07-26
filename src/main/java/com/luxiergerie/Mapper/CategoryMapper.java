package com.luxiergerie.Mapper;

import com.luxiergerie.DTO.CategoryDTO;
import com.luxiergerie.Model.Entity.Category;

public class CategoryMapper {
    public static CategoryDTO MappedCategoryFrom(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        categoryDTO.setImage(category.getImage());
        categoryDTO.setAccommodations(category.getAccommodations());
        categoryDTO.setSection(category.getSection());
        return categoryDTO;
    }

    public static Category MappedCategoryFrom(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setImage(categoryDTO.getImage());
        category.setAccommodations(categoryDTO.getAccommodations());
        category.setSection(categoryDTO.getSection());
        return category;
    }
}
