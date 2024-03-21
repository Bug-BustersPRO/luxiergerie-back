package com.luxiergerie.Domain.Entity;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

   @Column(name = "name")
  private String name;

  @Column(nullable= true, name = "description")
  private String description;

  @Column(name = "image")
  private String image;

  @OneToMany(mappedBy = "category")
  private List<Accommodation> accommodations;

  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "section_id")
  private Section section;


  public Category() {
  }


  public Category(String name, String image, List<Accommodation> accommodations, Section section) {
    this.name = name;
    this.image = image;
    this.accommodations = accommodations;
    this.section = section;
  }


  public Category(String name, String description, String image, List<Accommodation> accommodations, Section section) {
    this.name = name;
    this.description = description;
    this.image = image;
    this.accommodations = accommodations;
    this.section = section;
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

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
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
