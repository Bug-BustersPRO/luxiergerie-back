package com.luxiergerie.Domain.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "purchase")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "status")
    private String status;

    @ManyToMany
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinTable(
        name = "purchase_accomodation",
        joinColumns = @JoinColumn(name = "purchase_id"),
        inverseJoinColumns = @JoinColumn(name = "accomodation_id")
    )
    private List<Accomodation> accomodations = new ArrayList<>();

    public Purchase() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Accomodation> getAccomodations() {
        return accomodations;
    }

    public void setAccomodations(List<Accomodation> accomodations) {
        this.accomodations = accomodations;
    }

    public Purchase(UUID id, Date date, Room room, String status, List<Accomodation> accomodations) {
        this.id = id;
        this.date = date;
        this.room = room;
        this.status = status;
        this.accomodations = accomodations;
    }
}
