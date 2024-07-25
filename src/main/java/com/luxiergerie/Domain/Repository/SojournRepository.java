package com.luxiergerie.Domain.Repository;

import com.luxiergerie.Domain.Entity.Sojourn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SojournRepository extends JpaRepository<Sojourn, UUID> {
    Sojourn findBySojournIdentifier(String sojournIdentifier);
}
