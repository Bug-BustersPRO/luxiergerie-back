package com.luxiergerie.Model.Entity;

import com.luxiergerie.Model.Enums.SojournStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sojourn")
public class Sojourn {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "entry_date")
    private LocalDateTime entryDate;

    @Column(nullable = false, name = "exit_date")
    private LocalDateTime exitDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private SojournStatus status;

    @Column(nullable = false, name = "sojourn_identifier")
    private String sojournIdentifier;

    @Column(nullable = false, name = "pin")
    private int pin;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    // getters and setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDateTime entryDate) {
        this.entryDate = entryDate;
    }

    public LocalDateTime getExitDate() {
        return exitDate;
    }

    public void setExitDate(LocalDateTime exitDate) {
        this.exitDate = exitDate;
    }

    public SojournStatus getStatus() {
        return status;
    }

    public void setStatus(SojournStatus status) {
        this.status = status;
    }

    public String getSojournIdentifier() {
        return sojournIdentifier;
    }

    public void setSojournIdentifier(String sojournIdentifier) {
        this.sojournIdentifier = sojournIdentifier;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public Sojourn() {
    }
}