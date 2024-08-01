package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.RoomDTO;
import com.luxiergerie.Mapper.RoomMapper;
import com.luxiergerie.Model.Entity.Room;
import com.luxiergerie.Repository.RoleRepository;
import com.luxiergerie.Repository.RoomRepository;
import com.luxiergerie.Services.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    //ATTENTION A TESTER QUAND FRONT DISPO !!!!!!!!

    private final RoomRepository roomRepository;
    private final RoleRepository roleRepository;
    private final RoomService roomService;

    public RoomController(RoomRepository roomRepository, RoleRepository roleRepository, RoomService roomService) {
        this.roomRepository = roomRepository;
        this.roleRepository = roleRepository;
        this.roomService = roomService;
    }

    @GetMapping
    public List<RoomDTO> getRooms() {
        List<Room> rooms = this.roomRepository.findAll();
        return rooms.stream()
                .map(RoomMapper::MappedRoomFrom)
                .collect(toList());
    }

    @GetMapping("/available")
    public List<RoomDTO> getAvailableRooms() {
        List<Room> rooms = this.roomRepository.findAllRoomByClientIsNull();
        return rooms.stream()
                .map(RoomMapper::MappedRoomFrom)
                .collect(toList());
    }

    @PostMapping("/create-multiple/{maxRooms}")
    public ResponseEntity<List<RoomDTO>> createRooms(
            @RequestBody RoomDTO roomDTO,
            @PathVariable int maxRooms,
            @RequestParam(defaultValue = "100") int startRoomNumber) {
        try {
            roomService.createRooms(roomDTO, maxRooms, startRoomNumber);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @PostMapping
    public HttpStatus createRoom(@RequestBody RoomDTO roomDTO) {
        try {
            roomService.createRoom(roomDTO);
            return CREATED;
        } catch (Exception e) {
            return BAD_REQUEST;
        }
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<RoomDTO> updateRoom(@PathVariable UUID roomId, @RequestBody RoomDTO roomDTO) {
        try {
            RoomDTO updatedRoomDTO = roomService.updateRoom(roomId, roomDTO);
            return ResponseEntity.ok(updatedRoomDTO);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable UUID roomId) {
        try {
            roomService.deleteRoom(roomId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return new ResponseEntity<>(NOT_FOUND);
            } else if (e.getMessage().contains("permission")) {
                return new ResponseEntity<>(FORBIDDEN);
            } else {
                return new ResponseEntity<>(BAD_REQUEST);
            }
        }
    }

    @DeleteMapping("/delete-all")
    public void deleteAllRooms() {
        this.roomRepository.deleteAll();
    }

}