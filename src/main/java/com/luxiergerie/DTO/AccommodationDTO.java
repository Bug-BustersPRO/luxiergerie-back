package com.luxiergerie.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.luxiergerie.Model.Entity.Purchase;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

public class AccommodationDTO {

    private UUID id;
    private String name;
    private String description;
    private byte[] image;
    private BigDecimal price;
    private String priceToDisplay;
    private boolean isReservable;
    @Value("0")
    private Integer quantity;

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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
      return quantity;
    }

    public void setQuantity(Integer quantity) {
      this.quantity = quantity;
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
