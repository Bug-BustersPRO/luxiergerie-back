package com.luxiergerie.Mapper;

import com.luxiergerie.DTO.RoomDTO;
import com.luxiergerie.Model.Entity.Role;
import com.luxiergerie.Model.Entity.Room;

import static java.util.stream.Collectors.*;

public class RoomMapper {
    public static RoomDTO MappedRoomFrom(Room room) {
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
                    .map(SojournMapper::MappedSojournFrom)
                    .collect(toList()));
        }
        return dto;
    }

    public static Room MappedRoomFrom(RoomDTO dto, Role role) {
        Room room = new Room();
        room.setRoomNumber(dto.getRoomNumber());
        room.setFloor(dto.getFloor());
        room.setRole(role);
        if (dto.getSojourns() != null) {
            room.setSojourns(dto.getSojourns().stream()
                    .map(SojournMapper::MappedSojournFrom)
                    .collect(toList()));
        }
        return room;
    }
}