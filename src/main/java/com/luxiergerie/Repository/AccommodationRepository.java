package com.luxiergerie.Repository;

import com.luxiergerie.Model.Entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccommodationRepository extends JpaRepository<Accommodation, UUID> {

}