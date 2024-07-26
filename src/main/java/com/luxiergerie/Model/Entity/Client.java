package com.luxiergerie.Model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "first_name", length = 50)
    private String firstName;

    @Column(nullable = false, name = "last_name", length = 50)
    private String lastName;

    @Column(nullable = false, name = "email", length = 50)
    private String email;

    @Column(nullable = false, name = "phone_number", length = 50)
    private String phoneNumber;

    @Column(name = "pin")
    private int pin;

    @OneToOne(mappedBy = "client")
    @JsonIgnore
    private Room room;

    @JsonIgnore
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Purchase> purchases = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "client_authorities", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "authority")
    private List<String> authorities = new ArrayList<>();

    @OneToMany(mappedBy = "client")
    private List<Sojourn> sojourns;

    public Collection<? extends GrantedAuthority> getAuthorities() {
      if (Objects.isNull(this.authorities)) {
        return new ArrayList<>();
      }
        return this.authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public Client() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    public List<Sojourn> getSojourns() {
        return sojourns;
    }

    public void setSojourns(List<Sojourn> sojourns) {
        this.sojourns = sojourns;
    }

    public Client(UUID id, String firstName, String lastName, String email, String phoneNumber, int pin, Room room, List<Purchase> purchases) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.pin = pin;
        this.room = room;
        this.purchases = purchases;
        this.authorities = new ArrayList<>();
        this.sojourns = new ArrayList<>();
    }
}
