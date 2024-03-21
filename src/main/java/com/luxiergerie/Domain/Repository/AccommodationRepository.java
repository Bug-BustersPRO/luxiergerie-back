package com.luxiergerie.Domain.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luxiergerie.Domain.Entity.Accommodation;

public interface AccommodationRepository extends JpaRepository<Accommodation, UUID>{

}
