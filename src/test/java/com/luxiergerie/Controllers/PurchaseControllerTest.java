package com.luxiergerie.Controllers;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.luxiergerie.Domain.Entity.Purchase;
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
    purchases.add(new Purchase(UUID.randomUUID(), new Date(), "En cours"));
    purchases.add(new Purchase(UUID.randomUUID(), new Date(), "En attente"));
    when(purchaseRepository.findAll()).thenReturn(purchases);

    List<Purchase> result = purchaseController.getPurchases();

    assertEquals(purchases, result);
    verify(purchaseRepository, times(1)).findAll();
  }

  @Test
  public void testGetPurchase() {

    java.util.UUID purchaseId = java.util.UUID.randomUUID();
    Purchase purchase = new Purchase(purchaseId, new Date(), "Prêt");
    when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));

    Purchase result = purchaseController.getPurchase(purchaseId);

    assertEquals(purchase, result);
    verify(purchaseRepository, times(1)).findById(purchaseId);
  }

  @Test
  public void testGetPurchase_ThrowsExceptionIfIdIsNull() {
    // Arrange
    UUID purchaseId = null;

    // Act & Assert
    assertThrows(ResponseStatusException.class, () -> {
      purchaseController.getPurchase(purchaseId);
    });
  }

  @Test
  public void testCreatePurchase() {
    // Arrange
    Purchase purchase = new Purchase(null, new Date(), "En cours");
    when(purchaseRepository.save(purchase)).thenReturn(purchase);

    // Act
    Purchase result = purchaseController.createPurchase(purchase);

    // Assert
    assertEquals(purchase, result);
    verify(purchaseRepository, times(1)).save(purchase);
  }

  @Test
  public void testCreatePurchase_ThrowsExceptionIfIdIsNotNull() {
    // Arrange
    Purchase purchase = new Purchase(UUID.randomUUID(), new Date(), "En cours");

    // Act & Assert
    assertThrows(RuntimeException.class, () -> {
      purchaseController.createPurchase(purchase);
    });
  }

  @Test
  public void testUpdatePurchase() {
    // Arrange
    UUID purchaseId = UUID.randomUUID();
    Purchase existingPurchase = new Purchase(purchaseId, new Date(), "Commande reçu");
    Purchase updatedPurchase = new Purchase(purchaseId, new Date(), "Commande en cours");
    when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(existingPurchase));
    when(purchaseRepository.save(updatedPurchase)).thenReturn(updatedPurchase);

    // Act
    Purchase result = purchaseController.updatePurchase(purchaseId, updatedPurchase);

    // Assert
    assertEquals(updatedPurchase, result);
    verify(purchaseRepository, times(1)).findById(purchaseId);
    verify(purchaseRepository, times(1)).save(updatedPurchase);
  }

  @Test
  public void testUpdatePurchase_ThrowsExceptionIfPurchaseNotFound() {
    // Arrange
    UUID purchaseId = UUID.randomUUID();
    Purchase updatedPurchase = new Purchase(purchaseId, new Date(), "Commande en cours");
    when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(RuntimeException.class, () -> {
      purchaseController.updatePurchase(purchaseId, updatedPurchase);
    });
  }

  @Test
  public void testDeletePurchase() {
    UUID purchaseId = UUID.randomUUID();
    when(purchaseRepository.existsById(purchaseId)).thenReturn(true);

    purchaseController.deletePurchase(purchaseId);

    verify(purchaseRepository, times(1)).deleteById(purchaseId);
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