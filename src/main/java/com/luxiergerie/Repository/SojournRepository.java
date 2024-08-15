package com.luxiergerie.Repository;

import com.luxiergerie.Model.Entity.Sojourn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SojournRepository extends JpaRepository<Sojourn, UUID> {
    Sojourn findBySojournIdentifier(String sojournIdentifier);

}