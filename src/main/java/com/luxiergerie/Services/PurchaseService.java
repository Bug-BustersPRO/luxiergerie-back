package com.luxiergerie.Services;

import com.luxiergerie.DTO.BillDTO;
import com.luxiergerie.DTO.PurchaseDTO;
import com.luxiergerie.DTO.PurchaseForBillDTO;
import com.luxiergerie.Mapper.BillMapper;
import com.luxiergerie.Mapper.PurchaseMapper;
import com.luxiergerie.Model.Entity.Purchase;
import com.luxiergerie.Model.Entity.Room;
import com.luxiergerie.Repository.PurchaseRepository;
import com.luxiergerie.Repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;

import static com.luxiergerie.Mapper.PurchaseMapper.MappedPurchaseFrom;
import static java.math.BigDecimal.ZERO;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.http.HttpStatus.*;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final RoomRepository roomRepository;

    public PurchaseService(PurchaseRepository purchaseRepository, RoomRepository roomRepository) {
        this.purchaseRepository = purchaseRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public List<PurchaseDTO> getPurchases() {
        List<Purchase> purchases = purchaseRepository.findAll();
        List<Room> rooms = roomRepository.findAll();

        return getPurchaseDTOS(purchases)
                .stream()
                .map(purchaseDTO -> {
                    setRoomNumberIfClientIsNotNull(purchaseDTO, rooms);
                    return purchaseDTO;
                })
                .collect(toList());
    }

    private static void setRoomNumberIfClientIsNotNull(PurchaseDTO purchaseDTO, List<Room> rooms) {
        rooms.stream()
                .filter(room -> nonNull(room.getClient()) && room.getClient().getId().equals(purchaseDTO.getClient().getId()))
                .findFirst()
                .ifPresent(room -> purchaseDTO.setRoomNumber(room.getRoomNumber()));
    }

    private static List<PurchaseDTO> getPurchaseDTOS(List<Purchase> purchases) {
        return purchases.stream()
                .map(PurchaseMapper::MappedPurchaseFrom)
                .collect(toList());
    }

    @Transactional
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
                .filter(room -> nonNull(room.getClient()))
                .collect(toMap(room -> room.getClient().getId(), Room::getRoomNumber));

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
            if (isNull(totalPrice)) {
                totalPrice = ZERO;
            }
            billDTO.setTotalPrice(totalPrice);

            // Add the purchase to the list of purchases for the room number
            int roomNumber = billDTO.getRoomNumber();
            if (purchasesByRoom.containsKey(roomNumber)) {
                BillDTO existingBill = purchasesByRoom.get(roomNumber);
                BigDecimal existingTotalPrice = existingBill.getTotalPrice();
                if (isNull(existingTotalPrice)) {
                    existingTotalPrice = ZERO;
                }
                existingBill.setTotalPrice(existingTotalPrice.add(totalPrice));
                existingBill.getPurchasesForBillDTO().add(purchaseForBillDTO);
            } else {
                billDTO.getPurchasesForBillDTO().add(purchaseForBillDTO);
                purchasesByRoom.put(roomNumber, billDTO);
            }
        }
        return new ArrayList<>(purchasesByRoom.values());
    }

    @Transactional
    public PurchaseDTO getPurchase(@PathVariable("id") UUID purchaseId) {
        Optional<Purchase> purchaseOptional = purchaseRepository.findById(purchaseId);
        if (purchaseOptional.isPresent()) {
            return MappedPurchaseFrom(purchaseOptional.get());
        }
        throw new ResponseStatusException(NOT_FOUND, "Purchase not found with ID: " + purchaseId);
    }

    @Transactional
    public PurchaseDTO createPurchase(@RequestBody PurchaseDTO purchaseDTO) {
        if (isNull(purchaseDTO.getId())) {
            throw new ResponseStatusException(BAD_REQUEST, "Purchase ID cannot be null");
        }
        if (nonNull(purchaseDTO.getId())) {
            Optional<Purchase> purchaseOptional = purchaseRepository.findById(purchaseDTO.getId());
            if (purchaseOptional.isPresent()) {
                throw new ResponseStatusException(CONFLICT, "Purchase already exists with ID: " + purchaseDTO.getId());
            }
        }
        Purchase purchase = MappedPurchaseFrom(purchaseDTO);
        Purchase savedPurchase = purchaseRepository.save(purchase);
        return MappedPurchaseFrom(savedPurchase);
    }

    @Transactional
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
            throw new ResponseStatusException(NOT_FOUND, "Purchase not found with ID: " + id);
        }
    }

}