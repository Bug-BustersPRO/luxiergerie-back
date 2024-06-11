package com.luxiergerie.Domain.Mapper;

import com.luxiergerie.DTO.BillDTO;
import com.luxiergerie.DTO.PurchaseDTO;
import com.luxiergerie.DTO.PurchaseForBillDTO;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class BillMapper {
  public static BillDTO MappedPurchaseFrom(PurchaseForBillDTO purchaseForBillDTO, PurchaseDTO purchaseDTO) {
    BillDTO billDTO = new BillDTO();
    billDTO.setId(UUID.randomUUID());
    billDTO.setDate(new Date());
    billDTO.setClient(purchaseDTO.getClient());
    billDTO.setStatus(purchaseDTO.getStatus());
    billDTO.setRoomNumber(purchaseDTO.getRoomNumber());
    billDTO.getPurchasesForBillDTO().add(purchaseForBillDTO);

    return billDTO;
  }
}
