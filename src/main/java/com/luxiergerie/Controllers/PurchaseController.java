package com.luxiergerie.Controllers;

import com.luxiergerie.Domain.Entity.Purchase;
import com.luxiergerie.Domain.Repository.PurchaseRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class PurchaseController {

  private final PurchaseRepository purchaseRepository;

  public PurchaseController(PurchaseRepository purchaseRepository) {
    this.purchaseRepository = purchaseRepository;
  }

  @GetMapping("/purchases")
  public List<Purchase> getPurchases() {
    return purchaseRepository.findAll();
  }

  @GetMapping("/purchase/{id}")
  public Purchase getPurchase(@PathVariable("id") java.util.UUID purchaseId) {
    java.util.UUID nonNullId = Objects.requireNonNull(purchaseId, "Purchase must have an id");
    return this.purchaseRepository.findById(nonNullId)
        .orElseThrow(() -> new RuntimeException("Purchase not found with id:" + nonNullId));
  }


  @PostMapping("/purchase")
  public Purchase createPurchase(@RequestBody Purchase purchase) {
    if (purchase.getId() != null) {
      throw new RuntimeException("New Purchase must not have an id");
    } else {
      return purchaseRepository.save(purchase);
    }
  }

  @PutMapping("/purchase/{id}")
  public Purchase updatePurchase(@PathVariable("id") java.util.UUID purchaseId, @RequestBody Purchase purchase) {
    Optional<Purchase> purchaseOptional = purchaseRepository.findById(purchaseId);
    if (purchaseOptional.isPresent()) {
      Purchase purchaseToUpdate = purchaseOptional.get();
      purchaseToUpdate.setDate(purchase.getDate());
      purchaseToUpdate.setStatus(purchase.getStatus());
      return purchaseRepository.save(purchaseToUpdate);
    } else {
      throw new RuntimeException("Purchase not found with id:" + purchaseId);
    }
  }

  @DeleteMapping("/purchase/{id}")
  public void deletePurchase(@PathVariable("id") UUID id) {
    if (!purchaseRepository.existsById(id)) {
      throw new RuntimeException("Purchase not found with id:" + id);
    }
    purchaseRepository.deleteById(id);
  }
}
