package com.luxiergerie.Repository;

import com.luxiergerie.Model.Entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SectionRepository extends JpaRepository<Section, UUID> {

}