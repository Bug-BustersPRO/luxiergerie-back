package com.luxiergerie.Mapper;

import com.luxiergerie.DTO.RoomDTO;
import com.luxiergerie.Model.Entity.Role;
import com.luxiergerie.Model.Entity.Room;

import static java.util.stream.Collectors.toList;

public class RoomMapper {
    public static RoomDTO MappedRoomFrom(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setRoomNumber(room.getRoomNumber());
        roomDTO.setFloor(room.getFloor());
        roomDTO.setRole(room.getRole());
        if (room.getClient() != null) {
            roomDTO.setClient(room.getClient());
        }
        if (room.getSojourns() != null) {
            roomDTO.setSojourns(room.getSojourns().stream()
                    .map(SojournMapper::MappedSojournFrom)
                    .collect(toList()));
        }
        return roomDTO;
    }

    public static Room MappedRoomFrom(RoomDTO roomDTO, Role role) {
        Room room = new Room();
        room.setRoomNumber(roomDTO.getRoomNumber());
        room.setFloor(roomDTO.getFloor());
        room.setRole(role);
        if (roomDTO.getSojourns() != null) {
            room.setSojourns(roomDTO.getSojourns().stream()
                    .map(SojournMapper::MappedSojournFrom)
                    .collect(toList()));
        }
        return room;
    }

}