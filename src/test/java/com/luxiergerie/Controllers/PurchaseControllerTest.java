package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.PurchaseDTO;
import com.luxiergerie.Domain.Entity.Purchase;
import com.luxiergerie.Domain.Entity.Room;
import com.luxiergerie.Domain.Repository.PurchaseRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetPurchases() {
    UUID purchaseId = UUID.randomUUID();
    List<Purchase> purchases = new ArrayList<>();
    purchases.add(new Purchase(purchaseId, new Date(), new Room(), "En cours"));
    when(purchaseRepository.findAll()).thenReturn(purchases);

    List<PurchaseDTO> result = purchaseController.getPurchases();

    assertEquals(purchases.size(), result.size());
    verify(purchaseRepository).findAll();
  }

  @Test
  public void testGetPurchaseById() {
    UUID purchaseId = UUID.randomUUID();
    PurchaseDTO expectedDTO = new PurchaseDTO(purchaseId, new Date(), new Room(), "En cours", new ArrayList<>());
    Purchase purchase = new Purchase(purchaseId, expectedDTO.getDate(), expectedDTO.getRoom(), expectedDTO.getStatus());
    if (purchase != null) {
      purchase.setAccommodations(expectedDTO.getAccommodations());
    }
    when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));

    PurchaseDTO result = purchaseController.getPurchase(purchaseId);

    assertAll(
        () -> assertEquals(expectedDTO.getId(), result.getId()),
        () -> assertEquals(expectedDTO.getDate(), result.getDate()),
        () -> assertEquals(expectedDTO.getRoom(), result.getRoom()),
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
    PurchaseDTO purchaseDTO = new PurchaseDTO(purchaseId, new Date(), new Room(), "En cours", new ArrayList<>());
    Purchase expectedPurchase = new Purchase(purchaseId, purchaseDTO.getDate(), purchaseDTO.getRoom(), purchaseDTO.getStatus());
    if (expectedPurchase != null) {
      expectedPurchase.setAccommodations(purchaseDTO.getAccommodations());
    }
    when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());
    when(purchaseRepository.save(any(Purchase.class))).then(AdditionalAnswers.returnsFirstArg());

    PurchaseDTO result = purchaseController.createPurchase(purchaseDTO);

    assertNotNull(result.getId());
    assertEquals(purchaseDTO.getDate(), result.getDate());
    assertEquals(purchaseDTO.getRoom(), result.getRoom());
    assertEquals(purchaseDTO.getStatus(), result.getStatus());
    assertEquals(purchaseDTO.getAccommodations(), result.getAccommodations());

    verify(purchaseRepository).save(any(Purchase.class));
  }


  @Test
  public void testCreatePurchaseThrowsExceptionIfIdIsNull() {
    UUID purchaseId = null;
    when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      purchaseController.createPurchase(new PurchaseDTO(purchaseId, new Date(), new Room(), "En cours", new ArrayList<>()));
    });
  }

  @Test
  public void testUpdatePurchase() {
    UUID purchaseId = UUID.randomUUID();
    PurchaseDTO purchaseDTO = new PurchaseDTO(purchaseId, new Date(), new Room(), "En cours", new ArrayList<>());
    Purchase existingPurchase = new Purchase(purchaseId, new Date(), new Room(), "En cours", new ArrayList<>());
    Purchase updatedPurchase = new Purchase(purchaseId, purchaseDTO.getDate(), purchaseDTO.getRoom(), purchaseDTO.getStatus());
    if (updatedPurchase != null) {
      updatedPurchase.setAccommodations(purchaseDTO.getAccommodations());
    }
    when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(existingPurchase));
    when(purchaseRepository.save(any(Purchase.class))).then(AdditionalAnswers.returnsFirstArg());

    PurchaseDTO result = purchaseController.updatePurchase(purchaseId, purchaseDTO);

    assertAll(
        () -> assertEquals(purchaseDTO.getId(), result.getId()),
        () -> assertEquals(purchaseDTO.getDate(), result.getDate()),
        () -> assertEquals(purchaseDTO.getRoom(), result.getRoom()),
        () -> assertEquals(purchaseDTO.getStatus(), result.getStatus()),
        () -> assertEquals(purchaseDTO.getAccommodations(), result.getAccommodations())
    );
    verify(purchaseRepository).save(any(Purchase.class));
  }

  @Test
  public void testUpdatePurchaseThrowsExceptionIfPurchaseNotFound() {
    UUID purchaseId = UUID.randomUUID();
    Purchase purchase = new Purchase(purchaseId, new Date(), new Room(), "En cours");
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