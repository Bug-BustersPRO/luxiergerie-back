package com.luxiergerie.Domain.Repository;

import com.luxiergerie.Domain.Entity.Purchase;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;





public interface PurchaseRepository extends JpaRepository<Purchase, UUID>{

}
