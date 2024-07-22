package com.luxiergerie.Domain.Repository;

import com.luxiergerie.Domain.Entity.Room;
import com.luxiergerie.Domain.Entity.Sojourn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface SojournRepository extends JpaRepository<Sojourn, UUID> {
    List<Sojourn> findByRoomAndEntryDateLessThanEqualAndExitDateGreaterThanEqual(Room room, LocalDate exitDate, LocalDate entryDate);

}
