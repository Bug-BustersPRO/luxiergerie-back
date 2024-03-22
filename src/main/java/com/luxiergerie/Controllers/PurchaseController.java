package com.luxiergerie.Controllers;

import com.luxiergerie.Domain.Entity.Purchase;
import com.luxiergerie.Domain.Repository.PurchaseRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api")
public class PurchaseController {

  private final PurchaseRepository purchaseRepository;

  public PurchaseController(PurchaseRepository purchaseRepository) {
    this.purchaseRepository = purchaseRepository;
  }

  @GetMapping("/purchases")
  public List<Purchase> getPurchases() {
    return purchaseRepository.findAll();
  }

  @GetMapping("/purchases/{id}")
  public Purchase getPurchase(@PathVariable("id") UUID purchaseId) {
    Optional<Purchase> purchaseOptional = purchaseRepository.findById(purchaseId);
    if (purchaseOptional.isPresent()) {
        return purchaseOptional.get();
    } else {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase not found with ID: " + purchaseId);
    }
  }


  @PostMapping("/purchase")
  public Purchase createPurchase(@RequestBody Purchase purchase) {
    if (purchase.getId() != null) {
      throw new RuntimeException("New Purchase must not have an id");
    } else {
      return purchaseRepository.save(purchase);
    }
  }

  @PutMapping("/purchases/{id}")
  public Purchase updatePurchase(@PathVariable("id") UUID purchaseId, @RequestBody Purchase updatedPurchase) {
    Optional<Purchase> purchaseOptional = purchaseRepository.findById(purchaseId);
    if (purchaseOptional.isPresent()) {
      Purchase existingPurchase = purchaseOptional.get();
      existingPurchase.setDate(updatedPurchase.getDate());
      existingPurchase.setStatus(updatedPurchase.getStatus());
      existingPurchase.setRoom(updatedPurchase.getRoom());
      return purchaseRepository.save(existingPurchase);
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase not found with ID: " + purchaseId);
    }
  }


  @DeleteMapping("/purchases/{id}")
  public void deletePurchase(@PathVariable("id") UUID id) {
    if (!purchaseRepository.existsById(id)) {
      throw new RuntimeException("Purchase not found with id:" + id);
    }
    purchaseRepository.deleteById(id);
  }
}
