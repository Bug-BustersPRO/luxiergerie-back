package com.luxiergerie.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.luxiergerie.Domain.Entity.Purchase;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class AccommodationDTO {

    private UUID id;
    private String name;
    private String description;
    private String image;
    private BigDecimal price;
    private String priceToDisplay;
    private boolean isReservable;

    @JsonIgnore
    private UUID categoryId;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public UUID getCategory() {
        return categoryId;
    }

    public void setCategory(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public String getPriceToDisplay() {
        return priceToDisplay;
    }

    public void setPriceToDisplay(String priceToDisplay) {
        this.priceToDisplay = priceToDisplay;
    }

    public boolean isReservable() {
      return isReservable;
    }

    public void setReservable(boolean isReservable) {
      this.isReservable = isReservable;
    }

}
