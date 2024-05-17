package com.luxiergerie.Domain.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Accommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(nullable = true, name = "description")
    private String description;

    @Column(nullable = true, name = "image")
    private String image;

    @Column(name = "price")
    private Float price;

    @Column(name="is_reservable")
    private boolean isReservable;

    @Column(name="quantity")
    private Integer quantity = 0;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(mappedBy = "accommodations")
    @JsonIgnore
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

    public Integer getQuantity() {
      return quantity;
    }

    public void setQuantity(Integer quantity) {
      this.quantity = quantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isReservable() {
      return isReservable;
    }

    public void setReservable(boolean isReservable) {
      this.isReservable = isReservable;
    }

}
