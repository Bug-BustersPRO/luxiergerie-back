package com.luxiergerie.DTO;

import com.luxiergerie.Model.Entity.Client;
import com.luxiergerie.Model.Entity.Role;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RoomDTO {
    private UUID id;
    private int roomNumber;
    private int floor;
    private Role role;
    private Client client;
    private List<SojournDTO> sojourns;

    public RoomDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Role getRole() {
        return role;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<SojournDTO> getSojourns() {
        return sojourns;
    }

    public void setSojourns(List<SojournDTO> sojourns) {
        this.sojourns = sojourns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomDTO roomDTO = (RoomDTO) o;
        return roomNumber == roomDTO.roomNumber && floor == roomDTO.floor && Objects.equals(id, roomDTO.id) && Objects.equals(role, roomDTO.role) && Objects.equals(client, roomDTO.client);
    }

    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + roomNumber;
        result = 31 * result + floor;
        result = 31 * result + Objects.hashCode(role);
        result = 31 * result + Objects.hashCode(client);
        return result;
    }

}