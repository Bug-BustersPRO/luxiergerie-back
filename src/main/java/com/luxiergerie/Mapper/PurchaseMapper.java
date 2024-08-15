package com.luxiergerie.Mapper;

import com.luxiergerie.DTO.PurchaseDTO;
import com.luxiergerie.DTO.PurchaseForBillDTO;
import com.luxiergerie.Model.Entity.Accommodation;
import com.luxiergerie.Model.Entity.Purchase;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

public class PurchaseMapper {
    public static PurchaseDTO MappedPurchaseFrom(Purchase purchase) {
        PurchaseDTO purchaseDto = new PurchaseDTO();
        purchaseDto.setId(purchase.getId());
        purchaseDto.setDate(purchase.getDate());
        purchaseDto.setClient(purchase.getClient());
        purchaseDto.setStatus(purchase.getStatus());
        purchaseDto.setAccommodations(purchase.getAccommodations());
        BigDecimal totalPrice = purchase.getAccommodations().stream()
                .map(Accommodation::getPrice).reduce(BigDecimal::add).orElse(ZERO);
        purchaseDto.setTotalPrice(totalPrice);

        return purchaseDto;
    }

    public static Purchase MappedPurchaseFrom(PurchaseDTO purchaseDto) {
        Purchase purchase = new Purchase();
        purchase.setDate(purchaseDto.getDate());
        purchase.setClient(purchaseDto.getClient());
        purchase.setStatus(purchaseDto.getStatus());
        purchase.setAccommodations(purchaseDto.getAccommodations());
        purchase.setTotalPrice(purchaseDto.getTotalPrice());

        return purchase;
    }

    public static PurchaseForBillDTO MappedPurchaseForBillFrom(PurchaseDTO purchaseDTO) {
        PurchaseForBillDTO purchaseForBillDTO = new PurchaseForBillDTO();
        purchaseForBillDTO.setId(purchaseDTO.getId());
        purchaseForBillDTO.setDate(purchaseDTO.getDate());
        purchaseForBillDTO.setStatus(purchaseDTO.getStatus());
        purchaseForBillDTO.setAccommodations(purchaseDTO.getAccommodations());
        purchaseForBillDTO.setTotalPrice(purchaseDTO.getTotalPrice());

        return purchaseForBillDTO;
    }

}