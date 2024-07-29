package com.luxiergerie.Repository;

import com.luxiergerie.Model.Entity.Sojourn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SojournRepository extends JpaRepository<Sojourn, UUID> {
    Sojourn findBySojournIdentifier(String sojournIdentifier);

}