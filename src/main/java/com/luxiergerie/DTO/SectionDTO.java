package com.luxiergerie.DTO;

import java.util.List;
import java.util.UUID;

import com.luxiergerie.Domain.Entity.Category;

public class SectionDTO {

  private UUID id;
  private String name;
  private String image;
  private List<Category> categories;


  public SectionDTO() {
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public List<Category> getCategories() {
    return categories;
  }

  public void setCategories(List<Category> categories) {
    this.categories = categories;
  }

}
