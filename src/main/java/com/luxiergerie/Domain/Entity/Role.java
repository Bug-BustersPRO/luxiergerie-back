package com.luxiergerie.Domain.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;


@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;

  @ManyToMany(mappedBy = "roles")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private List<Employee> employees = new ArrayList<>();

  @OneToMany(mappedBy = "role")
  @JsonIgnore
  private List<Room> rooms = new ArrayList<>();

  /**
   * Gets the ID of the role.
   *
   * @return the ID of the role
   */
  public UUID getId() {
    return id;
  }

  /**
   * Sets the ID of the role.
   *
   * @param id the ID of the role
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Gets the name of the role.
   *
   * @return the name of the role
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the role.
   *
   * @param name the name of the role
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the list of employees associated with the role.
   *
   * @return the list of employees associated with the role
   */
  public List<Employee> getEmployees() {
    return employees;
  }

  /**
   * Sets the list of employees associated with the role.
   *
   * @param employees the list of employees associated with the role
   */
  public void setEmployees(List<Employee> employees) {
    this.employees = employees;
  }

    /**
     * Gets the list of rooms associated with the role.
     *
     * @return the list of rooms associated with the role
     */
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * Sets the list of rooms associated with the role.
     *
     * @param rooms the list of rooms associated with the role
     */
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

  /**
   * Constructs a new Role object with the specified ID, name, and employees.
   *
   * @param id        the ID of the role
   * @param name      the name of the role
   * @param employees the list of employees associated with the role
   */
  public Role(UUID id, String name, List<Employee> employees) {
    this.id = id;
    this.name = name;
    this.employees = employees;
  }

  /**
   * Constructs a new Role object.
   */
  public Role() {
  }

  @Override
  @JsonIgnore
  public String getAuthority() {
    return this.name;
  }


}