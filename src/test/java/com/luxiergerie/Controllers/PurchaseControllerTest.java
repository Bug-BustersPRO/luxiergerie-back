package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.PurchaseDTO;
import com.luxiergerie.Domain.Entity.Client;
import com.luxiergerie.Domain.Entity.Purchase;
import com.luxiergerie.Domain.Entity.Role;
import com.luxiergerie.Domain.Entity.Room;
import com.luxiergerie.Domain.Repository.PurchaseRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.luxiergerie.Domain.Repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



public class PurchaseControllerTest {

  @InjectMocks
  private PurchaseController purchaseController;

  @Mock
  private PurchaseRepository purchaseRepository;

  @Mock
  private RoomRepository roomRepository;

    @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetPurchases() {
    UUID purchaseId = UUID.randomUUID();
    List<Purchase> purchases = new ArrayList<>();
    List<Room> rooms = new ArrayList<>();

    when(roomRepository.findAll()).thenReturn(rooms);

    purchases.add(new Purchase(purchaseId, new Date(), new Client(), "En cours", new ArrayList<>()));
    when(purchaseRepository.findAll()).thenReturn(purchases);


    List<PurchaseDTO> result = purchaseController.getPurchases();

    assertEquals(purchases.size(), result.size());
    verify(purchaseRepository).findAll();
  }

  @Test
  public void testGetPurchaseById() {
    UUID purchaseId = UUID.randomUUID();
    PurchaseDTO expectedDTO = new PurchaseDTO(purchaseId, new Date(), new Client(), "En cours", new ArrayList<>(), 217, BigDecimal.valueOf(1000.00));
    Purchase purchase = new Purchase(purchaseId, expectedDTO.getDate(), expectedDTO.getClient(), expectedDTO.getStatus());
    if (purchase != null) {
      purchase.setAccommodations(expectedDTO.getAccommodations());
    }
    when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));

    PurchaseDTO result = purchaseController.getPurchase(purchaseId);

    assertAll(
        () -> assertEquals(expectedDTO.getId(), result.getId()),
        () -> assertEquals(expectedDTO.getDate(), result.getDate()),
        () -> assertEquals(expectedDTO.getClient(), result.getClient()),
        () -> assertEquals(expectedDTO.getStatus(), result.getStatus()),
        () -> assertEquals(expectedDTO.getAccommodations(), result.getAccommodations())
    );
    verify(purchaseRepository).findById(purchaseId);
  }

  @Test
  public void testGetPurchaseThrowsExceptionIfIdIsNull() {
    UUID purchaseId = null;

    assertThrows(ResponseStatusException.class, () -> {
      purchaseController.getPurchase(purchaseId);
    });
    verify(purchaseRepository).findById(purchaseId);
  }

  @Test
  public void testCreatePurchase() {
    UUID purchaseId = UUID.randomUUID();
    PurchaseDTO purchaseDTO = new PurchaseDTO(purchaseId, new Date(), new Client(), "En cours", new ArrayList<>(), 217, BigDecimal.valueOf(1000.00));
    Purchase expectedPurchase = new Purchase(purchaseId, purchaseDTO.getDate(), purchaseDTO.getClient(), purchaseDTO.getStatus());
    if (expectedPurchase != null) {
      expectedPurchase.setAccommodations(purchaseDTO.getAccommodations());
    }
    when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());
    when(purchaseRepository.save(any(Purchase.class))).then(AdditionalAnswers.returnsFirstArg());

    PurchaseDTO result = purchaseController.createPurchase(purchaseDTO);

    assertNotNull(result.getId());
    assertEquals(purchaseDTO.getDate(), result.getDate());
    assertEquals(purchaseDTO.getClient(), result.getClient());
    assertEquals(purchaseDTO.getStatus(), result.getStatus());
    assertEquals(purchaseDTO.getAccommodations(), result.getAccommodations());

    verify(purchaseRepository).save(any(Purchase.class));
  }


  @Test
  public void testCreatePurchaseThrowsExceptionIfIdIsNull() {
    UUID purchaseId = null;
    when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      purchaseController.createPurchase(new PurchaseDTO(purchaseId, new Date(), new Client(), "En cours", new ArrayList<>(), 217, BigDecimal.valueOf(1000.00)));
    });
  }

  @Test
  public void testUpdatePurchase() {
    UUID purchaseId = UUID.randomUUID();
    PurchaseDTO purchaseDTO = new PurchaseDTO(purchaseId, new Date(), new Client(), "En cours", new ArrayList<>(), 217, BigDecimal.valueOf(1000.00));
    Purchase existingPurchase = new Purchase(purchaseId, new Date(), new Client(), "En cours", new ArrayList<>());
    Purchase updatedPurchase = new Purchase(purchaseId, purchaseDTO.getDate(), purchaseDTO.getClient(), purchaseDTO.getStatus());
    if (updatedPurchase != null) {
      updatedPurchase.setAccommodations(purchaseDTO.getAccommodations());
    }
    when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(existingPurchase));
    when(purchaseRepository.save(any(Purchase.class))).then(AdditionalAnswers.returnsFirstArg());

    PurchaseDTO result = purchaseController.updatePurchase(purchaseId, purchaseDTO);

    assertAll(
        () -> assertEquals(purchaseDTO.getId(), result.getId()),
        () -> assertEquals(purchaseDTO.getDate(), result.getDate()),
        () -> assertEquals(purchaseDTO.getClient(), result.getClient()),
        () -> assertEquals(purchaseDTO.getStatus(), result.getStatus()),
        () -> assertEquals(purchaseDTO.getAccommodations(), result.getAccommodations())
    );
    verify(purchaseRepository).save(any(Purchase.class));
  }

  @Test
  public void testUpdatePurchaseThrowsExceptionIfPurchaseNotFound() {
    UUID purchaseId = UUID.randomUUID();
    Purchase purchase = new Purchase(purchaseId, new Date(), new Client(), "En cours");
    when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());
  }

  @Test
  public void testDeletePurchase() {
    UUID purchaseId = UUID.randomUUID();
    when(purchaseRepository.existsById(purchaseId)).thenReturn(true);

    purchaseController.deletePurchase(purchaseId);

    verify(purchaseRepository).deleteById(purchaseId);
  }

  @Test
   public void testDeletePurchase_ThrowsExceptionIfPurchaseNotFound() {
     UUID purchaseId = UUID.randomUUID();
     when(purchaseRepository.existsById(purchaseId)).thenReturn(false);

     assertThrows(RuntimeException.class, () -> {
       purchaseController.deletePurchase(purchaseId);
     });
  }
}