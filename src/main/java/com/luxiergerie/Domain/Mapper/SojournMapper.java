package com.luxiergerie.Domain.Mapper;

import com.luxiergerie.DTO.SojournDTO;
import com.luxiergerie.Domain.Entity.Sojourn;

import java.time.LocalTime;

public class SojournMapper {

    public static SojournDTO MappedSojournFrom(Sojourn sojourn) {
        SojournDTO dto = new SojournDTO();
        dto.setId(sojourn.getId());
        dto.setEntryDate(sojourn.getEntryDate().toLocalDate());
        dto.setExitDate(sojourn.getExitDate().toLocalDate());
        dto.setStatus(sojourn.getStatus());
        dto.setSojournIdentifier(sojourn.getSojournIdentifier());
        dto.setClientId(sojourn.getClient().getId());
        dto.setRoomRole(sojourn.getRoom().getRole());
        dto.setRoomId(sojourn.getRoom().getId());
        return dto;
    }

    public static Sojourn MappedSojournFrom(SojournDTO dto) {
        Sojourn sojourn = new Sojourn();
        sojourn.setEntryDate(dto.getEntryDate().atTime(LocalTime.of(14, 0)));
        sojourn.setExitDate(dto.getExitDate().atTime(LocalTime.of(11, 0)));
        sojourn.setStatus(dto.getStatus());
        sojourn.setSojournIdentifier(dto.getSojournIdentifier());
        sojourn.setPin(dto.getPin());
        return sojourn;
    }
}