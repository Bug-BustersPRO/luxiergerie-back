package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.PurchaseDTO;
import com.luxiergerie.Domain.Entity.Purchase;
import com.luxiergerie.Domain.Repository.PurchaseRepository;

import java.util.ArrayList;
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

import static com.luxiergerie.Domain.Mapper.PurchaseMapper.MappedPurchaseFrom;


@RestController
@RequestMapping("/api")
public class PurchaseController {

  private final PurchaseRepository purchaseRepository;

  public PurchaseController(PurchaseRepository purchaseRepository) {
    this.purchaseRepository = purchaseRepository;
  }

  @GetMapping("/purchases")
  public List<PurchaseDTO> getPurchases() {
    List<Purchase> purchases = purchaseRepository.findAll();
    List<PurchaseDTO> purchaseDTOs = new ArrayList<>();
    for (Purchase purchase : purchases) {
      purchaseDTOs.add(MappedPurchaseFrom(purchase));
    }
    return purchaseDTOs;
  }

  @GetMapping("/purchases/{id}")
  public PurchaseDTO getPurchase(@PathVariable("id") UUID purchaseId) {
    Optional<Purchase> purchaseOptional = purchaseRepository.findById(purchaseId);
    if (purchaseOptional.isPresent()) {
      return MappedPurchaseFrom(purchaseOptional.get());
    }
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase not found with ID: " + purchaseId);
  }


  @PostMapping("/purchase")
  public PurchaseDTO createPurchase(@RequestBody PurchaseDTO purchaseDTO) {
    Optional<Purchase> purchaseOptional = purchaseRepository.findById(purchaseDTO.getId());
    if(purchaseOptional.isPresent()){
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Purchase already exists with ID: " + purchaseDTO.getId());
    }
    Purchase purchase = MappedPurchaseFrom(purchaseDTO);
    Purchase savedPurchase = purchaseRepository.save(purchase);
    return MappedPurchaseFrom(savedPurchase);
  }

  @PutMapping("/purchases/{id}")
  public PurchaseDTO updatePurchase(@PathVariable("id") UUID id, @RequestBody PurchaseDTO purchaseDTO) {
    Optional<Purchase> purchaseOptional = purchaseRepository.findById(id);
    if (!purchaseOptional.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase not found with ID: " + id);
    }
    Purchase purchase = MappedPurchaseFrom(purchaseDTO);
    purchase.setId(id);
    Purchase savedPurchase = purchaseRepository.save(purchase);
    return MappedPurchaseFrom(savedPurchase);
  }


  @DeleteMapping("/purchases/{id}")
  public void deletePurchase(@PathVariable("id") UUID id) {
    if (!purchaseRepository.existsById(id)) {
      throw new RuntimeException("Purchase not found with id:" + id);
    }
    purchaseRepository.deleteById(id);
  }
}
