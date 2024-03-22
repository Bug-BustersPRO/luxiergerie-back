package com.luxiergerie.Domain.Mapper;

import com.luxiergerie.DTO.PurchaseDto;
import com.luxiergerie.Domain.Entity.Purchase;

public class PurchaseMapper {
  public static PurchaseDto toDto(Purchase purchase) {
    PurchaseDto purchaseDto = new PurchaseDto();
    purchaseDto.setId(purchase.getId());
    purchaseDto.setDate(purchase.getDate());
    purchaseDto.setRoom(purchase.getRoom());
    purchaseDto.setStatus(purchase.getStatus());
    purchaseDto.setAccommodations(purchase.getAccommodations());
    return purchaseDto;
  }

  public static Purchase toEntity(PurchaseDto purchaseDto) {
    Purchase purchase = new Purchase();
    purchase.setId(purchaseDto.getId());
    purchase.setDate(purchaseDto.getDate());
    purchase.setRoom(purchaseDto.getRoom());
    purchase.setStatus(purchaseDto.getStatus());
    purchase.setAccommodations(purchaseDto.getAccommodations());
    return purchase;
  }
}
