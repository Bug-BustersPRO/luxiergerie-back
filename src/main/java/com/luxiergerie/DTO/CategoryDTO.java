package com.luxiergerie.DTO;

import java.util.List;
import java.util.UUID;

import com.luxiergerie.Domain.Entity.Accommodation;
import com.luxiergerie.Domain.Entity.Section;

public class CategoryDTO {

  private UUID id;
  private String name;
  private String description;
  private byte[] image;
  private List<Accommodation> accommodations;
  private Section section;

   public CategoryDTO() {
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public byte[] getImage() {
    return image;
  }

  public void setImage(byte[] image) {
    this.image = image;
  }

  public List<Accommodation> getAccommodations() {
    return accommodations;
  }

  public void setAccommodations(List<Accommodation> accommodations) {
    this.accommodations = accommodations;
  }

  public Section getSection() {
    return section;
  }

  public void setSection(Section section) {
    this.section = section;
  }

}
