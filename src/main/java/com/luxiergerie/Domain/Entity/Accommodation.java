

package com.luxiergerie.Domain.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
public class Accommodation {

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

  @ManyToMany(mappedBy = "accommodations")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private List<Purchase> purchases = new ArrayList<>();

  public Accommodation() {
  }

  public Accommodation(String name, Float price, Category category) {
    this.name = name;
    this.price = price;
    this.category = category;
  }

  public Accommodation(String name, String description, Float price, Category category) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.category = category;
  }

  public Accommodation(String name, String description, String image, Float price, Category category) {
    this.name = name;
    this.description = description;
    this.image = image;
    this.price = price;
    this.category = category;
  }

  public Accommodation(UUID id, String name, String description, String image, Float price, Category category, List<Purchase> purchases) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.image = image;
    this.price = price;
    this.category = category;
    this.purchases = purchases;
  }

  public List<Purchase> getPurchases() {
    return purchases;
  }

  public void setPurchases(List<Purchase> purchases) {
    this.purchases = purchases;
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
