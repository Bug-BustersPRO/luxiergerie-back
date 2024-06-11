  package com.luxiergerie.DTO;

  import com.luxiergerie.Domain.Entity.Accommodation;
  import com.luxiergerie.Domain.Entity.Client;

  import java.util.ArrayList;
  import java.util.Date;
  import java.util.List;
  import java.util.UUID;


  public class PurchaseForBillDTO {
    private UUID id;
    private Date date;
    private String status;
    private List<Accommodation> accommodations = new ArrayList<>();
    private Float totalPrice;

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

    public Float getTotalPrice() {
      return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
      this.totalPrice = totalPrice;
    }

    public PurchaseForBillDTO() {
    }

    public PurchaseForBillDTO(UUID id, Date date, String status, List<Accommodation> accommodations, Float totalPrice) {
      this.id = id;
      this.date = date;
      this.status = status;
      this.accommodations = accommodations;
      this.totalPrice = totalPrice;
    }
  }
