package com.luxiergerie.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.luxiergerie.Model.Entity.Category;



public interface CategoryRepository extends JpaRepository<Category, UUID>{

  @Query(value = "SELECT category.name FROM category LEFT JOIN accommodation ON category.id = accommodation.category_id WHERE accommodation.id = :accommodationId", nativeQuery = true)
  public String getCategoryByAccommodation(@Param("accommodationId") UUID accommodationId);
}
