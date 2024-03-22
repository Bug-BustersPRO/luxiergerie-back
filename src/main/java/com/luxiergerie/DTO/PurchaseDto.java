package com.luxiergerie.DTO;

import com.luxiergerie.Domain.Entity.Accommodation;
import com.luxiergerie.Domain.Entity.Room;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class PurchaseDto {
  private UUID id;
  private Date date;
  private Room room;
  private String status;
  private List<Accommodation> accommodations = new ArrayList<>();

  public PurchaseDto() {
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

  public Room getRoom() {
    return room;
  }

  public void setRoom(Room room) {
    this.room = room;
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

}
