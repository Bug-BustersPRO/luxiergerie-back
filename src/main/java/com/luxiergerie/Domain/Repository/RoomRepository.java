package com.luxiergerie.Domain.Repository;

import com.luxiergerie.Domain.Entity.Role;
import com.luxiergerie.Domain.Entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {

    public Room findFirstByRoleAndClientIsNull(Role role);

    public List<Room> findAllRoomByClientIsNull();

    public Room findByRoomNumber(int roomNumber);

    public Room findByClient_Id(UUID id);
}
