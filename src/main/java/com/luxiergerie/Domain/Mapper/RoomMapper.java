package com.luxiergerie.Domain.Mapper;

import com.luxiergerie.DTO.RoomDTO;
import com.luxiergerie.Domain.Entity.Role;
import com.luxiergerie.Domain.Entity.Room;

public class RoomMapper {
    public static RoomDTO toDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setFloor(room.getFloor());
        dto.setRoleName(room.getRole().getName());
        return dto;
    }

    public static Room toEntity(RoomDTO dto, Role role) {
        Room room = new Room();
        room.setRoomNumber(dto.getRoomNumber());
        room.setFloor(dto.getFloor());
        room.setRole(role);
        return room;
    }
}