package com.luxiergerie.DTO;

import com.luxiergerie.Domain.Entity.Purchase;
import com.luxiergerie.Domain.Entity.Role;

import java.util.UUID;

public class ClientDTO {
    private UUID id;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private UUID room;
    private Role role;
    private Purchase purchase;

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public ClientDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UUID getRoom() {
        return room;
    }

    public void setRoom(UUID room) {
        this.room = room;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}