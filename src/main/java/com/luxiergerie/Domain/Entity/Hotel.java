package com.luxiergerie.Domain.Entity;


import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "name", length = 50)
    private String name;

    @Column(nullable = false, name = "image", columnDefinition = "LONGBLOB")
    @Lob
    private byte[] image;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "hotel_colors", joinColumns = @JoinColumn(name = "hotel_id"))
    @Column(nullable = false, name = "colors", length = 50)
    private List<String> colors;

    public Hotel() {
    }

    public Hotel(UUID id, String name, byte[] image, List<String> colors) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.colors = colors;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

}

