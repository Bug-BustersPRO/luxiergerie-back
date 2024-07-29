package com.luxiergerie.Services;

import com.luxiergerie.DTO.RoomDTO;
import com.luxiergerie.Mapper.RoomMapper;
import com.luxiergerie.Model.Entity.Role;
import com.luxiergerie.Model.Entity.Room;
import com.luxiergerie.Repository.RoleRepository;
import com.luxiergerie.Repository.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

import static com.luxiergerie.Mapper.RoomMapper.MappedRoomFrom;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoleRepository roleRepository;

    public RoomService(RoomRepository roomRepository, RoleRepository roleRepository) {
        this.roomRepository = roomRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public List<RoomDTO> createRooms(@RequestBody RoomDTO roomDTO, @PathVariable int maxRooms, @RequestParam(defaultValue = "100") int startRoomNumber) {
        List<Room> existingRooms = this.roomRepository.findAll();
        int existingRoomCount = existingRooms.size();
        Role existingRole;

        if (Objects.equals("ROLE_DIAMOND", roomDTO.getRole().getName())) {
            existingRole = roleRepository.findByName("ROLE_DIAMOND");
        } else {
            existingRole = roleRepository.findByName("ROLE_GOLD");
        }
        if (isNull(existingRole)) {
            throw new RuntimeException("Role not found with name: " + roomDTO.getRole().getName());
        }

        if (existingRoomCount >= maxRooms) {
            throw new RuntimeException("Cannot create more rooms. Hotel is at maximum capacity.");
        }

        int maxRoomNumber = existingRooms.stream()
                .mapToInt(Room::getRoomNumber)
                .max()
                .orElse(startRoomNumber - 1);
        startRoomNumber = Math.max(startRoomNumber, maxRoomNumber + 1);

        List<Room> rooms = new ArrayList<>();
        int roomsToCreate = maxRooms - existingRoomCount;
        for (int i = 0; i < roomsToCreate; i++) {
            int roomNumber = startRoomNumber + i;
            RoomDTO roomDto = new RoomDTO();
            roomDto.setRoomNumber(roomNumber);
            roomDto.setFloor(roomDTO.getFloor());
            roomDto.setRole(roomDto.getRole());
            Room room = MappedRoomFrom(roomDto, existingRole);
            rooms.add(room);
        }

        List<Room> savedRooms = this.roomRepository.saveAll(rooms);
        return savedRooms.stream().map(RoomMapper::MappedRoomFrom).collect(toList());
    }

    @Transactional
    public HttpStatus createRoom(@RequestBody RoomDTO roomDTO) {
        Optional<Room> roomOptional = ofNullable(roomRepository.findByRoomNumber(roomDTO.getRoomNumber()));
        Role role = this.roleRepository.findByName(roomDTO.getRole().getName());
        if (isNull(role)) {
            throw new RuntimeException("Role not found with name: " + roomDTO.getRole().getName());
        }
        if (roomOptional.isPresent() && roomOptional.get().getRoomNumber() == roomDTO.getRoomNumber()) {
            throw new RuntimeException("Room already exists with number: " + roomDTO.getRoomNumber());
        }
        Room room = MappedRoomFrom(roomDTO, role);
        this.roomRepository.save(room);

        return CREATED;
    }

    @Transactional
    public RoomDTO updateRoom(@PathVariable UUID roomId, @RequestBody RoomDTO roomDTO) {
        Role role = this.roleRepository.findByName(roomDTO.getRole().getName());
        if (isNull(role)) {
            throw new RuntimeException("Role not found with name: " + roomDTO.getRole().getName());
        }
        Optional<Room> roomToChange = this.roomRepository.findById(roomId);
        if (roomToChange.isPresent()) {
            Room room = roomToChange.get();
            room.setRoomNumber(roomDTO.getRoomNumber());
            room.setFloor(roomDTO.getFloor());
            room.setRole(role);
            room.setClient(roomDTO.getClient());
            this.roomRepository.save(room);
            return MappedRoomFrom(room);
        } else {
            throw new RuntimeException("Room not found with id: " + roomId);
        }
    }

    public void deleteRoom(@PathVariable UUID roomId) {
        if (!this.roomRepository.existsById(roomId)) {
            throw new RuntimeException("Room not found with id: " + roomId);
        }
        this.roomRepository.deleteById(roomId);
    }

}