package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.PurchaseDTO;
import com.luxiergerie.Domain.Entity.Accommodation;
import com.luxiergerie.Domain.Entity.Purchase;
import com.luxiergerie.Domain.Entity.Room;
import com.luxiergerie.Domain.Mapper.PurchaseMapper;
import com.luxiergerie.Domain.Repository.PurchaseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.luxiergerie.Domain.Repository.RoomRepository;
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
import static java.util.stream.Collectors.*;


@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

  private final PurchaseRepository purchaseRepository;
  private final RoomRepository roomRepository;

  public PurchaseController(PurchaseRepository purchaseRepository, RoomRepository roomRepository) {
    this.purchaseRepository = purchaseRepository;
      this.roomRepository = roomRepository;
  }

  @GetMapping("")
  public List<PurchaseDTO> getPurchases() {
    List<Purchase> purchases = purchaseRepository.findAll();
    List<Room> rooms = roomRepository.findAll();

    return purchases.stream()
            .map(PurchaseMapper::MappedPurchaseFrom)
            .map(purchaseDTO -> {
              rooms.stream()
                      .filter(room -> room.getClient().getId().equals(purchaseDTO.getClient().getId()))
                      .findFirst()
                      .ifPresent(room -> purchaseDTO.setRoomNumber(room.getRoomNumber()));

              // Calculate the total price
              Float totalPrice = (float) purchaseDTO.getAccommodations().stream()
                      .mapToDouble(Accommodation::getPrice)
                      .sum();
              purchaseDTO.setTotalPrice(totalPrice);

              return purchaseDTO;
            })
            .collect(Collectors.toList());
  }


  @GetMapping("/{id}")
  public PurchaseDTO getPurchase(@PathVariable("id") UUID purchaseId) {
    Optional<Purchase> purchaseOptional = purchaseRepository.findById(purchaseId);
    if (purchaseOptional.isPresent()) {
      return MappedPurchaseFrom(purchaseOptional.get());
    }
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase not found with ID: " + purchaseId);
  }


  @PostMapping("")
  public PurchaseDTO createPurchase(@RequestBody PurchaseDTO purchaseDTO) {
    Optional<Purchase> purchaseOptional = purchaseRepository.findById(purchaseDTO.getId());
    if(purchaseOptional.isPresent()){
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Purchase already exists with ID: " + purchaseDTO.getId());
    }
    Purchase purchase = MappedPurchaseFrom(purchaseDTO);
    Purchase savedPurchase = purchaseRepository.save(purchase);
    return MappedPurchaseFrom(savedPurchase);
  }

  @PutMapping("/{id}")
  public PurchaseDTO updatePurchase(@PathVariable("id") UUID id, @RequestBody PurchaseDTO purchaseDTO) {
    Optional<Purchase> purchaseOptional = purchaseRepository.findById(id);
    if (purchaseOptional.isPresent()) {
        Purchase purchase = purchaseOptional.get();
        purchase.setDate(purchaseDTO.getDate());
        purchase.setClient(purchaseDTO.getClient());
        purchase.setStatus(purchaseDTO.getStatus());
        purchase.setAccommodations(purchaseDTO.getAccommodations());
        Purchase updatedPurchase = purchaseRepository.save(purchase);
        return MappedPurchaseFrom(updatedPurchase);
        } else {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase not found with ID: " + id);
      }
  }

  @DeleteMapping("/{id}")
  public void deletePurchase(@PathVariable("id") UUID id) {
    if (!purchaseRepository.existsById(id)) {
      throw new RuntimeException("Purchase not found with id:" + id);
    }
    purchaseRepository.deleteById(id);
  }
}