package com.luxiergerie.Domain.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "blacklisted_token")
public class BlackListedToken {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, name = "token", length = 600)
  private String token;

  @Column(nullable = false, name = "expiry_date")
  private Instant expiryDate;

  @Column(nullable = false, name = "user_id")
  private UUID userId;

  @Column(nullable = false, name = "isBlackListed")
  private boolean isBlackListed;

  /**
   * Default constructor for the BlackListedToken class.
   */
  public BlackListedToken() {
  }

  /**
   * Constructor for the BlackListedToken class.
   *
   * @param id         The ID of the blacklisted token.
   * @param token      The blacklisted token.
   * @param expiryDate The expiry date of the blacklisted token.
   * @param userId     The ID of the user associated with the blacklisted token.
   */
  public BlackListedToken(UUID id, String token, Instant expiryDate, UUID userId, boolean isBlackListed) {
    this.id = id;
    this.token = token;
    this.expiryDate = expiryDate;
    this.userId = userId;
    this.isBlackListed = isBlackListed;
  }

  /**
   * Get the ID of the blacklisted token.
   *
   * @return The ID of the blacklisted token.
   */
  public UUID getId() {
    return id;
  }

  /**
   * Set the ID of the blacklisted token.
   *
   * @param id The ID of the blacklisted token.
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Get the blacklisted token.
   *
   * @return The blacklisted token.
   */
  public String getToken() {
    return token;
  }

  /**
   * Set the blacklisted token.
   *
   * @param token The blacklisted token.
   */
  public void setToken(String token) {
    this.token = token;
  }

  /**
   * Get the expiry date of the blacklisted token.
   *
   * @return The expiry date of the blacklisted token.
   */
  public Instant getExpiryDate() {
    return expiryDate;
  }

  /**
   * Set the expiry date of the blacklisted token.
   *
   * @param expiryDate The expiry date of the blacklisted token.
   */
  public void setExpiryDate(Instant expiryDate) {
    this.expiryDate = expiryDate;
  }

  /**
   * Get the ID of the user associated with the blacklisted token.
   *
   * @return The ID of the user associated with the blacklisted token.
   */
  public UUID getUserId() {
    return userId;
  }

  /**
   * Set the ID of the user associated with the blacklisted token.
   *
   * @param userId The ID of the user associated with the blacklisted token.
   */
  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  /**
   * Get the blacklisted status of the token.
   *
   * @return The blacklisted status of the token.
   */
  public boolean isBlackListed() {
    return isBlackListed;
  }

  /**
   * Set the blacklisted status of the token.
   *
   * @param blackListed The blacklisted status of the token.
   */
  public void setBlackListed(boolean blackListed) {
    isBlackListed = blackListed;
  }
}
