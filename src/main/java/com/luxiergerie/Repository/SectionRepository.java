package com.luxiergerie.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luxiergerie.Model.Entity.Section;

public interface SectionRepository extends JpaRepository<Section, UUID>{

}
