package com.luxiergerie.Model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "number")
    private int roomNumber;

    @Column(nullable = false, name = "floor")
    private int floor;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @OneToMany(mappedBy = "room")
    private List<Sojourn> sojourns;

    public Room() {
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

    public void setRole(Role role) {
        this.role = role;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Sojourn> getSojourns() {
        return sojourns;
    }

    public void setSojourns(List<Sojourn> sojourns) {
        this.sojourns = sojourns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;
        return roomNumber == room.roomNumber && floor == room.floor && Objects.equals(id, room.id) && Objects.equals(role, room.role) && Objects.equals(client, room.client);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + roomNumber;
        result = 31 * result + floor;
        result = 31 * result + Objects.hashCode(role);
        result = 31 * result + Objects.hashCode(client);
        return result;
    }

    public Room findByClient_Id(UUID id) {
        return this;
    }

}
