package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.RoomDTO;
import com.luxiergerie.Domain.Entity.Role;
import com.luxiergerie.Domain.Entity.Room;
import com.luxiergerie.Domain.Mapper.RoomMapper;
import com.luxiergerie.Domain.Repository.RoleRepository;
import com.luxiergerie.Domain.Repository.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomRepository roomRepository;
    private final RoleRepository roleRepository;

    public RoomController(RoomRepository roomRepository, RoleRepository roleRepository) {
        this.roomRepository = roomRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public List<RoomDTO> getRooms() {
        List<Room> rooms = this.roomRepository.findAll();
        return rooms.stream()
                .map(RoomMapper::toDTO)
                .collect(toList());
    }

    @GetMapping("/available")
    public List<RoomDTO> getAvailableRooms() {
        List<Room> rooms = this.roomRepository.findAllRoomByClientIsNull();
        return rooms.stream()
                .map(RoomMapper::toDTO)
                .collect(toList());
    }

    @PostMapping("/create-multiple/{maxRooms}")
    public List<RoomDTO> createRooms(@RequestBody RoomDTO roomDTO, @PathVariable int maxRooms, @RequestParam(defaultValue = "100") int startRoomNumber) {
        List<Room> existingRooms = this.roomRepository.findAll();
        int existingRoomCount = existingRooms.size();
        Role existingRole;

        if (Objects.equals("ROLE_DIAMOND", roomDTO.getRole().getName())) {
            existingRole = roleRepository.findByName("ROLE_DIAMOND");
        } else {
            existingRole = roleRepository.findByName("ROLE_GOLD");
        }
        if (existingRole == null) {
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
            Room room = RoomMapper.toEntity(roomDto, existingRole);
            rooms.add(room);
        }

        List<Room> savedRooms = this.roomRepository.saveAll(rooms);
        return savedRooms.stream().map(RoomMapper::toDTO).collect(toList());
    }

    @PostMapping
    public HttpStatus createSpecificRoom(@RequestBody RoomDTO roomDTO) {
        Optional<Room> roomOptional = Optional.ofNullable(roomRepository.findByRoomNumber(roomDTO.getRoomNumber()));
        Role role = this.roleRepository.findByName(roomDTO.getRole().getName());
        if (role == null) {
            throw new RuntimeException("Role not found with name: " + roomDTO.getRole().getName());
        }
        if (roomOptional.isPresent() && roomOptional.get().getRoomNumber() == roomDTO.getRoomNumber()) {
            throw new RuntimeException("Room already exists with number: " + roomDTO.getRoomNumber());
        }
        Room room = RoomMapper.toEntity(roomDTO, role);
        this.roomRepository.save(room);

        return HttpStatus.CREATED;
    }

    @PutMapping("/{roomId}")
    public RoomDTO updateRoom(@PathVariable UUID roomId, @RequestBody RoomDTO roomDTO) {
        Role role = this.roleRepository.findByName(roomDTO.getRole().getName());
        if (role == null) {
            throw new RuntimeException("Role not found with name: " + roomDTO.getRole().getName());
        }
        Optional<Room> roomToChange = this.roomRepository.findById(roomId);
        if (roomToChange.isPresent()){
            Room room = roomToChange.get();
            room.setRoomNumber(roomDTO.getRoomNumber());
            room.setFloor(roomDTO.getFloor());
            room.setRole(roomDTO.getRole());
            room.setClient(roomDTO.getClient());
            this.roomRepository.save(room);
            return RoomMapper.toDTO(room);
        } else {
            throw new RuntimeException("Room not found with id: " + roomId);
        }
    }

    @DeleteMapping("/{roomId}")
    public void deleteRoom(@PathVariable UUID roomId) {
        if (!this.roomRepository.existsById(roomId)) {
            throw new RuntimeException("Room not found with id: " + roomId);
        }
        this.roomRepository.deleteById(roomId);
    }

    @DeleteMapping("/delete-all")
    public void deleteAllRooms() {
        this.roomRepository.deleteAll();
    }
}
