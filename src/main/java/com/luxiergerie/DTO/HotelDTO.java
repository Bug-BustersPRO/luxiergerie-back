package com.luxiergerie.DTO;

import java.util.List;
import java.util.UUID;

public class HotelDTO {

    private UUID id;
    private String name;
    private byte[] image;
    private List<String> colors;

    public HotelDTO(){

    }

    public HotelDTO(UUID id, String name, byte[] image, List<String> colors) {
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
