

package com.luxiergerie.Domain.Entity;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Accomodation {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "name")
  private String name;

  @Column(nullable= true, name = "description")
  private String description;

  @Column(nullable = true, name = "image")
  private String image;

  @Column(name = "price")
  private Float price;

  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "category_id")
  private Category category;


  public Accomodation() {
  }

  public Accomodation(String name, Float price, Category category) {
    this.name = name;
    this.price = price;
    this.category = category;
  }

  public Accomodation(String name, String description, Float price, Category category) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.category = category;
  }

  public Accomodation(String name, String description, String image, Float price, Category category) {
    this.name = name;
    this.description = description;
    this.image = image;
    this.price = price;
    this.category = category;
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

  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
    this.price = price;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

}
