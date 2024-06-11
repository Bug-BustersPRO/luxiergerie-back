package com.luxiergerie.Domain.Repository;

import com.luxiergerie.Domain.Entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PurchaseRepository extends JpaRepository<Purchase, UUID> { }