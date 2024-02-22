package com.luxiergerie.Domain.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.UUID;


@Entity
@Table(name = "blacklisted_token")
public class BlackListedToken {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, name = "token")
  private String token;

  @Column(nullable = false, name = "expiry_date")
  private Date expiryDate;

  @Column(nullable = false, name = "user_id")
  private UUID userId;

  public BlackListedToken() {
  }

  public BlackListedToken(UUID id, String token, Date expiryDate, UUID userId) {
    this.id = id;
    this.token = token;
    this.expiryDate = expiryDate;
    this.userId = userId;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Date getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }
}
