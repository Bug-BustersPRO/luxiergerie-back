package com.luxiergerie.Domain.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "purchase")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "status")
    private String status;

    @ManyToMany
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinTable(
        name = "purchase_accommodation",
        joinColumns = @JoinColumn(name = "purchase_id"),
        inverseJoinColumns = @JoinColumn(name = "accommodation_id")
    )
    private List<Accommodation> accommodations = new ArrayList<>();

    @Column(name = "totalPrice")
    private BigDecimal totalPrice;

    public Purchase() {
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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
        calculateTotalPrice();
    }

    public BigDecimal getTotalPrice() {
      return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
      this.totalPrice = totalPrice;
    }

    private void calculateTotalPrice() {
      this.totalPrice = accommodations.stream()
          .map(Accommodation::getPrice)
          .reduce(BigDecimal::add)
          .orElse(BigDecimal.ZERO);
  }

    public Purchase(UUID id, Date date, Client client, String status, List<Accommodation> accommodations) {
      this.id = id;
      this.date = date;
      this.client = client;
      this.status = status;
      this.accommodations = accommodations;
    }

    public Purchase(UUID id, Date date, Client client, String status) {
      this.id = id;
      this.date = date;
      this.client = client;
      this.status = status;
    }

}
