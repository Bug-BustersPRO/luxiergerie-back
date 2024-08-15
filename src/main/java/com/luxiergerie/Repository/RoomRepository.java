package com.luxiergerie.Repository;

import com.luxiergerie.Model.Entity.Role;
import com.luxiergerie.Model.Entity.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {
    public Room findFirstByRoleAndClientIsNull(Role role);

    public List<Room> findAllRoomByClientIsNull();

    public Room findByRoomNumber(int roomNumber);

    public Room findByClient_Id(UUID id);

    @Query("SELECT r FROM Room r WHERE r.role.name = :roleName AND " +
            "(r.sojourns IS EMPTY OR " +
            "NOT EXISTS (SELECT s FROM Sojourn s WHERE s.room = r AND s.entryDate <= :exitDate AND s.exitDate >= :entryDate))")
    List<Room> findAvailableRoomsByRoleName(@Param("roleName") String roleName, @Param("entryDate") LocalDateTime entryDate, @Param("exitDate") LocalDateTime exitDate, Pageable pageable);

}