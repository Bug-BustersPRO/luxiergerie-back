package com.luxiergerie.Domain.Repository;

import com.luxiergerie.Domain.Entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
}
