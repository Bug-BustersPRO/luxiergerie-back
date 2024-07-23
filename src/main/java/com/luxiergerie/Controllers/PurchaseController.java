package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.BillDTO;
import com.luxiergerie.DTO.PurchaseDTO;
import com.luxiergerie.DTO.PurchaseForBillDTO;
import com.luxiergerie.Domain.Entity.Purchase;
import com.luxiergerie.Domain.Entity.Room;
import com.luxiergerie.Domain.Mapper.BillMapper;
import com.luxiergerie.Domain.Mapper.PurchaseMapper;
import com.luxiergerie.Domain.Repository.PurchaseRepository;

import java.math.BigDecimal;
import java.util.*;
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
                return purchaseDTO;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/billByClient")
    public List<BillDTO> getBillByClient() {
        // Fetch all purchases and rooms
        List<Purchase> purchases = purchaseRepository.findAll();
        List<PurchaseDTO> purchaseDTOs = purchases.stream()
                .map(PurchaseMapper::MappedPurchaseFrom)
                .toList();

        List<Room> rooms = roomRepository.findAll();

        // Create a map of client IDs to room numbers for quick lookup
        Map<UUID, Integer> clientIdToRoomNumber = rooms.stream()
                .collect(Collectors.toMap(room -> room.getClient().getId(), Room::getRoomNumber));

        // Process the purchases
        Map<Integer, BillDTO> purchasesByRoom = new HashMap<>();
        for (PurchaseDTO purchaseDTO : purchaseDTOs) {
            PurchaseForBillDTO purchaseForBillDTO = PurchaseMapper.MappedPurchaseForBillFrom(purchaseDTO);
            BillDTO billDTO = BillMapper.MappedPurchaseFrom(purchaseForBillDTO, purchaseDTO);

            // Set the room number using the map
            UUID clientId = purchaseDTO.getClient().getId();
            if (clientIdToRoomNumber.containsKey(clientId)) {
                billDTO.setRoomNumber(clientIdToRoomNumber.get(clientId));
            }

            // Set the total price for the bill from the purchases
            BigDecimal totalPrice = purchaseForBillDTO.getTotalPrice();
            if (totalPrice == null) {
              totalPrice = BigDecimal.ZERO;
            }
            billDTO.setTotalPrice(totalPrice);

            // Add the purchase to the list of purchases for the room number
            int roomNumber = billDTO.getRoomNumber();
            if (purchasesByRoom.containsKey(roomNumber)) {
                BillDTO existingBill = purchasesByRoom.get(roomNumber);
                BigDecimal existingTotalPrice = existingBill.getTotalPrice();
                if (existingTotalPrice == null) {
                    existingTotalPrice = BigDecimal.ZERO;
                }
                existingBill.setTotalPrice(existingTotalPrice.add(totalPrice));
                existingBill.getPurchasesForBillDTO().add(purchaseForBillDTO);
            } else {
                billDTO.getPurchasesForBillDTO().add(purchaseForBillDTO);
                purchasesByRoom.put(roomNumber, billDTO);
            }
        }

        // Convert the map values to a list and return
        return new ArrayList<>(purchasesByRoom.values());
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
    if(Objects.nonNull(purchaseDTO.getId())) {
      Optional<Purchase> purchaseOptional = purchaseRepository.findById(purchaseDTO.getId());
      if(purchaseOptional.isPresent()){
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Purchase already exists with ID: " + purchaseDTO.getId());
      }
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
        purchase.setTotalPrice(purchaseDTO.getTotalPrice());
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
