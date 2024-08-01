package com.luxiergerie.DTO;

import com.luxiergerie.Model.Entity.Accommodation;
import com.luxiergerie.Model.Entity.Client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PurchaseDTO {
    private UUID id;
    private Date date;
    private Client client;
    private String status;
    private List<Accommodation> accommodations = new ArrayList<>();
    private int roomNumber;
    private BigDecimal totalPrice;

    public PurchaseDTO(UUID id, Date date, Client client, String status, List<Accommodation> accommodations, int roomNumber, BigDecimal totalPrice) {
        this.id = id;
        this.date = date;
        this.client = client;
        this.status = status;
        this.accommodations = accommodations;
        this.roomNumber = roomNumber;
        this.totalPrice = totalPrice;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Accommodation> getAccommodations() {
        return accommodations;
    }

    public void setAccommodations(List<Accommodation> accommodations) {
        this.accommodations = accommodations;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public PurchaseDTO(UUID id, Date date, Client client, String status, List<Accommodation> accommodations, int roomNumber) {
        this.id = id;
        this.date = date;
        this.client = client;
        this.status = status;
        this.accommodations = accommodations;
        this.roomNumber = roomNumber;
    }

    public PurchaseDTO() {
    }

}
