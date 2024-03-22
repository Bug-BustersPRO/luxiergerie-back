package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.RoomDTO;
import com.luxiergerie.Domain.Entity.Role;
import com.luxiergerie.Domain.Entity.Room;
import com.luxiergerie.Domain.Mapper.RoomMapper;
import com.luxiergerie.Domain.Repository.RoleRepository;
import com.luxiergerie.Domain.Repository.RoomRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    private final RoomRepository roomRepository;
    private final RoleRepository roleRepository;

    public RoomController(RoomRepository roomRepository, RoleRepository roleRepository) {
        this.roomRepository = roomRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public List<Room> getRooms() {
        return this.roomRepository.findAll();
    }

    @GetMapping("/available")
    public List<Room> getAvailableRooms() {
        return this.roomRepository.findAllRoomByClientIsNull();
    }

    @PostMapping("/create-multiple/{maxRooms}")
    public List<RoomDTO> createRooms(@RequestBody RoomDTO roomDTO, @PathVariable int maxRooms, @RequestParam(defaultValue = "100") int startRoomNumber) {
        List<Room> existingRooms = this.roomRepository.findAll();
        int existingRoomCount = existingRooms.size();
        Role existingRole;

        if (Objects.equals("ROLE_DIAMOND", roomDTO.getRoleName())) {
            existingRole = roleRepository.findByName("ROLE_DIAMOND");
        } else {
            existingRole = roleRepository.findByName("ROLE_GOLD");
        }
        if (existingRole == null) {
            throw new RuntimeException("Role not found with id: " + roomDTO.getRoleName());
        }

        if (existingRoomCount >= maxRooms) {
            throw new RuntimeException("Cannot create more rooms. Hotel is at maximum capacity.");
        }

        int maxRoomNumber = existingRooms.stream().mapToInt(Room::getRoomNumber).max().orElse(startRoomNumber - 1);
        startRoomNumber = Math.max(startRoomNumber, maxRoomNumber + 1);

        List<Room> rooms = new ArrayList<>();
        int roomsToCreate = maxRooms - existingRoomCount;
        for (int i = 0; i < roomsToCreate; i++) {
            int roomNumber = startRoomNumber + i;
            RoomDTO roomDto = new RoomDTO();
            roomDto.setRoomNumber(roomNumber);
            roomDto.setFloor(roomDTO.getFloor());
            roomDto.setRoleName(roomDto.getRoleName());
            Room room = RoomMapper.toEntity(roomDto, existingRole);
            rooms.add(room);
        }

        List<Room> savedRooms = this.roomRepository.saveAll(rooms);
        return savedRooms.stream().map(RoomMapper::toDTO).collect(Collectors.toList());
    }

    @PostMapping
    public Room createSpecificRoom(@RequestBody RoomDTO roomDTO) {
        Role role = this.roleRepository.findByName(roomDTO.getRoleName());
        if (role == null) {
            throw new RuntimeException("Role not found with name: " + roomDTO.getRoleName());
        }
        Room room = RoomMapper.toEntity(roomDTO, role);
        return this.roomRepository.save(room);
    }

    @PutMapping("/{roomId}")
    public Room updateRoom(@PathVariable UUID roomId, @RequestBody RoomDTO roomDTO) {
        Role role = this.roleRepository.findByName(roomDTO.getRoleName());
        if (role == null) {
            throw new RuntimeException("Role not found with name: " + roomDTO.getRoleName());
        }
        Room room = this.roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
        room.setRoomNumber(roomDTO.getRoomNumber());
        room.setFloor(roomDTO.getFloor());
        room.setRole(role);
        return this.roomRepository.save(room);
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
