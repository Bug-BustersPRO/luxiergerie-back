package com.luxiergerie.Domain.Mapper;

import com.luxiergerie.DTO.SojournDTO;
import com.luxiergerie.Domain.Entity.Sojourn;

public class SojournMapper {

    public static SojournDTO toDTO(Sojourn sojourn) {
        SojournDTO dto = new SojournDTO();
        dto.setId(sojourn.getId());
        dto.setEntryDate(sojourn.getEntryDate());
        dto.setExitDate(sojourn.getExitDate());
        dto.setStatus(sojourn.getStatus());
        dto.setClientId(sojourn.getClient().getId());
        dto.setRoomId(sojourn.getRoom().getId());
        return dto;
    }

    public static Sojourn toEntity(SojournDTO dto) {
        Sojourn sojourn = new Sojourn();
        sojourn.setEntryDate(dto.getEntryDate());
        sojourn.setExitDate(dto.getExitDate());
        sojourn.setStatus(dto.getStatus());
        return sojourn;
    }
}