package com.luxiergerie.DTO;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.luxiergerie.Domain.Entity.Category;
import com.luxiergerie.Domain.Entity.Purchase;

public class AccommodationDTO {

  private UUID id;
  private String name;
  private String description;
  private String image;
  private Float price;
  private String priceToDisplay;

  @JsonIgnore
  private CategoryDTO category;
  @JsonIgnore
  private List<Purchase> purchases;

  public AccommodationDTO() {
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

  public CategoryDTO getCategory() {
    return category;
  }

  public void setCategory(CategoryDTO category) {
    this.category = category;
  }

  public String getPriceToDisplay() {
    return priceToDisplay;
  }

  public void setPriceToDisplay(String priceToDisplay) {
    this.priceToDisplay = priceToDisplay;
  }

}
