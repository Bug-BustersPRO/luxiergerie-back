package com.luxiergerie.Controllers;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    List<Purchase> purchases = new ArrayList<>();
    purchases.add(new Purchase(UUID.randomUUID(), new Date(), new Room(), "En cours"));
    purchases.add(new Purchase(UUID.randomUUID(), new Date(), new Room(), "Prêt"));
    when(purchaseRepository.findAll()).thenReturn(purchases);

    List<Purchase> result = purchaseController.getPurchases();

    assertEquals(purchases, result);
    verify(purchaseRepository, times(1)).findAll();
  }

  @Test
  public void testGetPurchase() {
    UUID purchaseId = UUID.randomUUID();
    Purchase purchase = new Purchase(purchaseId, new Date(), new Room(), "Prêt");
    when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));

    Purchase result = purchaseController.getPurchase(purchaseId);

    assertEquals(purchase, result);
    verify(purchaseRepository, times(1)).findById(purchaseId);
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
    Purchase purchase = new Purchase(null, new Date(), new Room(), "En cours");
    when(purchaseRepository.save(purchase)).thenReturn(purchase);

    Purchase result = purchaseController.createPurchase(purchase);

    assertEquals(purchase, result);
    verify(purchaseRepository).save(purchase);
  }

  @Test
  public void testCreatePurchaseThrowsExceptionIfIdIsNotNull() {
    Purchase purchase = new Purchase(UUID.randomUUID(), new Date(), new Room(), "En cours");

    assertThrows(RuntimeException.class, () -> {
      purchaseController.createPurchase(purchase);
    });
  }

  @Test
  public void testUpdatePurchase() {
    UUID purchaseId = UUID.randomUUID();
    Room room = new Room();
    Purchase existingPurchase = new Purchase(purchaseId, new Date(), room, "En cours");
    Purchase updatedPurchase = new Purchase(purchaseId, new Date(), room, "Validée");
    when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(existingPurchase));
    when(purchaseRepository.save(existingPurchase)).thenReturn(updatedPurchase);

    Purchase result = purchaseController.updatePurchase(purchaseId, updatedPurchase);

    assertEquals(updatedPurchase, result);
    verify(purchaseRepository).save(existingPurchase);
  }

  @Test
  public void testUpdatePurchaseThrowsExceptionIfPurchaseNotFound() {
    UUID purchaseId = UUID.randomUUID();
    Purchase updatedPurchase = new Purchase(purchaseId, new Date(), new Room(), "Commande en cours");
    when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      purchaseController.updatePurchase(purchaseId, updatedPurchase);
    });
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