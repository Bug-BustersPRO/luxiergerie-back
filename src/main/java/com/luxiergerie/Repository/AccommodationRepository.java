package com.luxiergerie.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luxiergerie.Model.Entity.Accommodation;

public interface AccommodationRepository extends JpaRepository<Accommodation, UUID>{


}
