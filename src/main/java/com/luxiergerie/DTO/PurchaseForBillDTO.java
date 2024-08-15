package com.luxiergerie.DTO;

import com.luxiergerie.Model.Entity.Accommodation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PurchaseForBillDTO {
    private UUID id;
    private Date date;
    private String status;
    private List<Accommodation> accommodations = new ArrayList<>();
    private BigDecimal totalPrice;

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

    public PurchaseForBillDTO() {
    }

    public PurchaseForBillDTO(UUID id, Date date, String status, List<Accommodation> accommodations, BigDecimal totalPrice) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.accommodations = accommodations;
        this.totalPrice = totalPrice;
    }

}