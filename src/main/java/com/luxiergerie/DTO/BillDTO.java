  package com.luxiergerie.DTO;

  import com.luxiergerie.Domain.Entity.Client;

  import java.util.ArrayList;
  import java.util.Date;
  import java.util.List;
  import java.util.UUID;


  public class BillDTO {
    private UUID id;
    private Date date;
    private Client client;
    private String status;
    private int roomNumber;
    private Float totalPrice;
    private List<PurchaseForBillDTO> purchasesForBillDTO = new ArrayList<>();

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

    public int getRoomNumber() {
      return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
      this.roomNumber = roomNumber;
    }

    public Float getTotalPrice() {
      return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
      this.totalPrice = totalPrice;
    }

    public List<PurchaseForBillDTO> getPurchasesForBillDTO() {
      return purchasesForBillDTO;
    }

    public void setPurchasesForBillDTO(List<PurchaseForBillDTO> purchasesForBillDTO) {
      this.purchasesForBillDTO = purchasesForBillDTO;
    }

    public BillDTO(UUID id, Date date, Client client, String status, int roomNumber, Float totalPrice, List<PurchaseForBillDTO> purchasesForBillDTO) {
      this.id = id;
      this.date = date;
      this.client = client;
      this.status = status;
      this.roomNumber = roomNumber;
      this.totalPrice = totalPrice;
      this.purchasesForBillDTO = purchasesForBillDTO;
    }

    public BillDTO() {
    }
  }
