package com.luxiergerie.Model.Entity;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

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

    @Column(nullable = false, name = "isblacklisted")
    private Boolean isblacklisted;


    public BlackListedToken() {
    }

    public BlackListedToken(UUID id, String token, Instant expiryDate, UUID userId, Boolean isblacklisted) {
        this.id = id;
        this.token = token;
        this.expiryDate = expiryDate;
        this.userId = userId;
        this.isblacklisted = isblacklisted;
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

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Boolean getBlackListed() {
        return isblacklisted;
    }

    public void setBlackListed(Boolean isblacklisted) {
        this.isblacklisted = isblacklisted;
    }

}