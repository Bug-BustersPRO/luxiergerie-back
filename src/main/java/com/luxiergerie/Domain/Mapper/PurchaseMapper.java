package com.luxiergerie.Domain.Mapper;

import com.luxiergerie.DTO.PurchaseDTO;
import com.luxiergerie.Domain.Entity.Purchase;
import com.luxiergerie.Domain.Entity.Room;

public class PurchaseMapper {
  public static PurchaseDTO MappedPurchaseFrom(Purchase purchase) {
    PurchaseDTO purchaseDto = new PurchaseDTO();
    purchaseDto.setId(purchase.getId());
    purchaseDto.setDate(purchase.getDate());
    purchaseDto.setClient(purchase.getClient());
    purchaseDto.setStatus(purchase.getStatus());
    purchaseDto.setAccommodations(purchase.getAccommodations());
    return purchaseDto;
  }

  public static Purchase MappedPurchaseFrom(PurchaseDTO purchaseDto) {
    Purchase purchase = new Purchase();
    purchase.setId(purchaseDto.getId());
    purchase.setDate(purchaseDto.getDate());
    purchase.setClient(purchaseDto.getClient());
    purchase.setStatus(purchaseDto.getStatus());
    purchase.setAccommodations(purchaseDto.getAccommodations());
    return purchase;
  }
}
