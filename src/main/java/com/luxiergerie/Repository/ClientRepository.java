package com.luxiergerie.Repository;

import com.luxiergerie.Model.Entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

}