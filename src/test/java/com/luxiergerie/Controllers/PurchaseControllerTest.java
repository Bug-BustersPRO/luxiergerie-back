package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.PurchaseDTO;
import com.luxiergerie.Model.Entity.Client;
import com.luxiergerie.Model.Entity.Purchase;
import com.luxiergerie.Repository.PurchaseRepository;
import com.luxiergerie.Repository.RoomRepository;
import com.luxiergerie.Services.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

public class PurchaseControllerTest {

    @InjectMocks
    private PurchaseController purchaseController;
    @Mock
    private PurchaseService purchaseService;

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
        List<PurchaseDTO> purchaseDTOs = Collections.singletonList(
                new PurchaseDTO(UUID.randomUUID(), new Date(), new Client(), "En cours", new ArrayList<>(), 217, BigDecimal.valueOf(1000.00))
        );
        when(purchaseService.getPurchases()).thenReturn(purchaseDTOs);

        List<PurchaseDTO> result = purchaseController.getPurchases();

        assertEquals(purchaseDTOs.size(), result.size());
        verify(purchaseService).getPurchases();
    }

    @Test
    public void testGetPurchaseById() {
        UUID purchaseId = UUID.randomUUID();
        PurchaseDTO expectedDTO = new PurchaseDTO(purchaseId, new Date(), new Client(), "En cours", new ArrayList<>(), 217, BigDecimal.valueOf(1000.00));
        Purchase purchase = new Purchase(purchaseId, expectedDTO.getDate(), expectedDTO.getClient(), expectedDTO.getStatus());
        purchase.setAccommodations(expectedDTO.getAccommodations());

        when(purchaseService.getPurchase(purchaseId)).thenReturn(expectedDTO);

        ResponseEntity<PurchaseDTO> response = purchaseController.getPurchase(purchaseId);
        PurchaseDTO result = response.getBody();

        assertNotNull(result);
        assertAll(
                () -> assertEquals(expectedDTO.getId(), result.getId()),
                () -> assertEquals(expectedDTO.getDate(), result.getDate()),
                () -> assertEquals(expectedDTO.getClient(), result.getClient()),
                () -> assertEquals(expectedDTO.getStatus(), result.getStatus()),
                () -> assertEquals(expectedDTO.getAccommodations(), result.getAccommodations())
        );
        verify(purchaseService).getPurchase(purchaseId);
    }

    @Test
    public void testGetPurchaseThrowsExceptionIfIdIsNull() {
        UUID purchaseId = null;

        assertThrows(ResponseStatusException.class, () -> {
            purchaseController.getPurchase(purchaseId);
        });

    }

    @Test
    public void testCreatePurchase() {
        UUID purchaseId = UUID.randomUUID();
        PurchaseDTO purchaseDTO = new PurchaseDTO(purchaseId, new Date(), new Client(), "En cours", new ArrayList<>(), 217, BigDecimal.valueOf(1000.00));
        Purchase expectedPurchase = new Purchase(purchaseId, purchaseDTO.getDate(), purchaseDTO.getClient(), purchaseDTO.getStatus());
        expectedPurchase.setAccommodations(purchaseDTO.getAccommodations());

        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(expectedPurchase);

        ResponseEntity<PurchaseDTO> response = purchaseController.createPurchase(purchaseDTO);

        assertNotNull(response);
        assertEquals(CREATED, response.getStatusCode());

    }

    @Test
    public void testUpdatePurchase_Success() {
        // Arrange
        UUID purchaseId = UUID.randomUUID();
        PurchaseDTO purchaseDTO = new PurchaseDTO(purchaseId, new Date(), new Client(), "En cours", new ArrayList<>(), 217, BigDecimal.valueOf(1000.00));
        Purchase existingPurchase = new Purchase(purchaseId, new Date(), new Client(), "En cours", new ArrayList<>());
        Purchase updatedPurchase = new Purchase(purchaseId, purchaseDTO.getDate(), purchaseDTO.getClient(), purchaseDTO.getStatus());

        updatedPurchase.setAccommodations(purchaseDTO.getAccommodations());

        when(purchaseService.updatePurchase(purchaseId, purchaseDTO)).thenReturn(purchaseDTO);
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(existingPurchase));
        when(purchaseRepository.save(any(Purchase.class))).then(AdditionalAnswers.returnsFirstArg());

        // Act
        ResponseEntity<PurchaseDTO> response = purchaseController.updatePurchase(purchaseId, purchaseDTO);

        // Assert
        assertEquals(OK, response.getStatusCode());

    }

    @Test
    public void testUpdatePurchase_BadRequest() {
        // Arrange
        UUID purchaseId = UUID.randomUUID();
        PurchaseDTO purchaseDTO = new PurchaseDTO(purchaseId, new Date(), new Client(), "En cours", new ArrayList<>(), 217, BigDecimal.valueOf(1000.00));

        // Simulate an IllegalArgumentException being thrown by the service
        doThrow(new IllegalArgumentException()).when(purchaseService).updatePurchase(any(UUID.class), any(PurchaseDTO.class));

        // Act
        ResponseEntity<PurchaseDTO> response = purchaseController.updatePurchase(purchaseId, purchaseDTO);

        // Assert
        assertAll(
                () -> assertEquals(BAD_REQUEST, response.getStatusCode())
        );
    }

    @Test
    public void testUpdatePurchaseThrowsExceptionIfPurchaseNotFound() {
        UUID purchaseId = randomUUID();
        Purchase purchase = new Purchase(purchaseId, new Date(), new Client(), "En cours");
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());
    }

    @Test
    public void testDeletePurchase() {
        UUID purchaseId = randomUUID();
        when(purchaseRepository.existsById(purchaseId)).thenReturn(true);

        purchaseController.deletePurchase(purchaseId);

        verify(purchaseRepository).deleteById(purchaseId);
    }

    @Test
    public void testDeletePurchase_ThrowsExceptionIfPurchaseNotFound() {
        UUID purchaseId = randomUUID();
        when(purchaseRepository.existsById(purchaseId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            purchaseController.deletePurchase(purchaseId);
        });
    }

}