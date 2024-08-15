package com.luxiergerie.DTO;

import com.luxiergerie.Model.Entity.Role;
import com.luxiergerie.Model.Enums.SojournStatus;

import java.time.LocalDate;
import java.util.UUID;

public class SojournDTO {
    private UUID id;
    private LocalDate entryDate;
    private LocalDate exitDate;
    private SojournStatus status;
    private String sojournIdentifier;
    private int pin;
    private UUID clientId;
    private Role roleRoom;
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

    public String getSojournIdentifier() {
        return sojournIdentifier;
    }

    public void setSojournIdentifier(String sojournIdentifier) {
        this.sojournIdentifier = sojournIdentifier;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public Role getRoomRole() {
        return roleRoom;
    }

    public void setRoomRole(Role roleRoom) {
        this.roleRoom = roleRoom;
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