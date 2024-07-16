package com.luxiergerie.Domain.Entity;

import com.luxiergerie.Domain.Enums.SojournStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "sojourn")
public class Sojourn {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "entry_date")
    private LocalDate entryDate;

    @Column(nullable = false, name = "exit_date")
    private LocalDate exitDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private SojournStatus status;

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

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public LocalDate getExitDate() {
        return exitDate;
    }

    public void setExitDate(LocalDate exitDate) {
        this.exitDate = exitDate;
    }

    public SojournStatus getStatus() {
        return status;
    }

    public void setStatus(SojournStatus status) {
        this.status = status;
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

    public Sojourn() {
    }
}