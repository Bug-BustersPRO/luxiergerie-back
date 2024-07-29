package com.luxiergerie.Model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
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

    @Column(nullable = false, name = "image", columnDefinition = "LONGBLOB")
    @Lob
    private byte[] image;

    @Column(name = "price")
    private BigDecimal price;

    @Value("false")
    @Column(name = "is_reservable")
    private boolean isReservable;

    @Column(name = "quantity")
    private Integer quantity = 0;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(mappedBy = "accommodations", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Purchase> purchases = new ArrayList<>();

    public Accommodation() {
    }

    public Accommodation(String name, BigDecimal price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Accommodation(String name, String description, BigDecimal price, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    public Accommodation(String name, String description, byte[] image, BigDecimal price, Category category) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.category = category;
    }

    public Accommodation(UUID id, String name, String description, byte[] image, BigDecimal price, Category category, List<Purchase> purchases) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.category = category;
        this.purchases = purchases;
    }

    public Accommodation(UUID id, String name, String description, byte[] image, BigDecimal price, boolean isReservable, Integer quantity, Category category, List<Purchase> purchases) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.isReservable = isReservable;
        this.quantity = quantity;
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