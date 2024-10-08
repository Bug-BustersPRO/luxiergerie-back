package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.BillDTO;
import com.luxiergerie.DTO.PurchaseDTO;
import com.luxiergerie.Repository.PurchaseRepository;
import com.luxiergerie.Repository.RoomRepository;
import com.luxiergerie.Services.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    //IMPOSSIBLE DE TESTER EN L'ETAT !!!!!!!!!!!!!!!

    private final PurchaseRepository purchaseRepository;
    private final RoomRepository roomRepository;
    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseRepository purchaseRepository, RoomRepository roomRepository, PurchaseService purchaseService) {
        this.purchaseRepository = purchaseRepository;
        this.roomRepository = roomRepository;
        this.purchaseService = purchaseService;
    }

    @GetMapping("")
    public List<PurchaseDTO> getPurchases() {
        return purchaseService.getPurchases();
    }

    @GetMapping("/billByClient")
    public ResponseEntity<List<BillDTO>> getBillByClient() {
        List<BillDTO> bills = purchaseService.getBillByClient();
        return new ResponseEntity<>(bills, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDTO> getPurchase(@PathVariable("id") UUID purchaseId) {
        if (isNull(purchaseId)) {
            throw new ResponseStatusException(BAD_REQUEST, "Purchase ID cannot be null");
        }
        PurchaseDTO purchaseDTO = purchaseService.getPurchase(purchaseId);
        return ResponseEntity.ok(purchaseDTO);
    }

    @PostMapping("")
    public ResponseEntity<PurchaseDTO> createPurchase(@RequestBody PurchaseDTO purchaseDTO) {
        try {
            purchaseService.createPurchase(purchaseDTO);
            return new ResponseEntity<>(CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseDTO> updatePurchase(@PathVariable("id") UUID id, @RequestBody PurchaseDTO purchaseDTO) {
        try {
            purchaseService.updatePurchase(id, purchaseDTO);
            return new ResponseEntity<>(OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(NOT_FOUND);
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