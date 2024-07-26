package com.luxiergerie.Model.Entity;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "name")
  private String name;

  @Column(nullable= true, name = "description")
  private String description;

  @Column(nullable = false, name = "image", columnDefinition = "LONGBLOB")
  @Lob
  private byte[] image;

  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<Accommodation> accommodations;

  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "section_id")
  private Section section;


  public Category() {
  }

  public Category(UUID id, String name, String description, byte[] image, List<Accommodation> accommodations, Section section) {
    this.id = id;
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
