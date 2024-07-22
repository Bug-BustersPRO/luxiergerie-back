package com.luxiergerie.Domain.Mapper;

import com.luxiergerie.DTO.RoomDTO;
import com.luxiergerie.Domain.Entity.Role;
import com.luxiergerie.Domain.Entity.Room;

import java.util.stream.Collectors;

public class RoomMapper {
    public static RoomDTO toDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setFloor(room.getFloor());
        dto.setRole(room.getRole());
        if(room.getClient() != null)
        {
            dto.setClient(room.getClient());
        }
        if (room.getSojourns() != null) {
            dto.setSojourns(room.getSojourns().stream()
                    .map(SojournMapper::toDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public static Room toEntity(RoomDTO dto, Role role) {
        Room room = new Room();
        room.setRoomNumber(dto.getRoomNumber());
        room.setFloor(dto.getFloor());
        room.setRole(role);
        if (dto.getSojourns() != null) {
            room.setSojourns(dto.getSojourns().stream()
                    .map(SojournMapper::toEntity)
                    .collect(Collectors.toList()));
        }
        return room;
    }
}