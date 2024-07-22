package com.luxiergerie.DTO;

import com.luxiergerie.Domain.Enums.SojournStatus;

import java.time.LocalDate;
import java.util.UUID;

public class SojournDTO {
    private UUID id;
    private LocalDate entryDate;
    private LocalDate exitDate;
    private SojournStatus status;
    private UUID clientId;
    private UUID roomId;

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

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }

    public SojournDTO() {
    }
}