package com.luxiergerie.Repository;

import com.luxiergerie.Model.Entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, UUID> {

}