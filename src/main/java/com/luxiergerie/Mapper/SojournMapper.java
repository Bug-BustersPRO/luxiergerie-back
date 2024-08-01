package com.luxiergerie.Mapper;

import com.luxiergerie.DTO.SojournDTO;
import com.luxiergerie.Model.Entity.Sojourn;

import java.time.LocalTime;

public class SojournMapper {
    public static SojournDTO MappedSojournFrom(Sojourn sojourn) {
        SojournDTO sojournDTO = new SojournDTO();
        sojournDTO.setId(sojourn.getId());
        sojournDTO.setEntryDate(sojourn.getEntryDate().toLocalDate());
        sojournDTO.setExitDate(sojourn.getExitDate().toLocalDate());
        sojournDTO.setStatus(sojourn.getStatus());
        sojournDTO.setSojournIdentifier(sojourn.getSojournIdentifier());
        sojournDTO.setClientId(sojourn.getClient().getId());
        sojournDTO.setRoomRole(sojourn.getRoom().getRole());
        sojournDTO.setRoomId(sojourn.getRoom().getId());
        return sojournDTO;
    }

    public static Sojourn MappedSojournFrom(SojournDTO sojournDTO) {
        Sojourn sojourn = new Sojourn();
        sojourn.setEntryDate(sojournDTO.getEntryDate().atTime(LocalTime.of(14, 0)));
        sojourn.setExitDate(sojournDTO.getExitDate().atTime(LocalTime.of(11, 0)));
        sojourn.setStatus(sojournDTO.getStatus());
        sojourn.setSojournIdentifier(sojournDTO.getSojournIdentifier());
        sojourn.setPin(sojournDTO.getPin());
        return sojourn;
    }

}