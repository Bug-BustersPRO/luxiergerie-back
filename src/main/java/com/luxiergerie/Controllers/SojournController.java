package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.SojournDTO;
import com.luxiergerie.Domain.Entity.Client;
import com.luxiergerie.Domain.Entity.Room;
import com.luxiergerie.Domain.Entity.Sojourn;
import com.luxiergerie.Domain.Mapper.SojournMapper;
import com.luxiergerie.Domain.Repository.ClientRepository;
import com.luxiergerie.Domain.Repository.RoomRepository;
import com.luxiergerie.Domain.Repository.SojournRepository;
import com.luxiergerie.Services.SojournService;
import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sojourns")
public class SojournController {

    private final SojournRepository sojournRepository;
    private final SojournService sojournService;


    public SojournController(SojournRepository sojournRepository, SojournService sojournService) {
        this.sojournRepository = sojournRepository;
        this.sojournService = sojournService;
    }

    @GetMapping
    public List<SojournDTO> getSojourns() {
        List<Sojourn> sojourns = this.sojournRepository.findAll();
        return sojourns.stream()
                .map(SojournMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public HttpStatus createSojourn(@RequestBody SojournDTO sojournDTO) {
        this.sojournService.createSojourn(sojournDTO);
        return HttpStatus.CREATED;
    }

    @PutMapping("/{sojournId}")
    public SojournDTO updateSojourn(@PathVariable UUID sojournId, @RequestBody SojournDTO sojournDTO) {
        Sojourn sojourn = this.sojournRepository.findById(sojournId).orElseThrow(() -> new RuntimeException("Sojourn not found with id: " + sojournId));
        sojourn.setEntryDate(sojournDTO.getEntryDate());
        sojourn.setExitDate(sojournDTO.getExitDate());
        sojourn.setStatus(sojournDTO.getStatus());
        sojourn.setClient(this.sojournService.getClient(sojournDTO.getClientId()));
        sojourn.setRoom(this.sojournService.getRoom(sojournDTO.getRoomId()));
        this.sojournRepository.save(sojourn);
        return SojournMapper.toDTO(sojourn);
    }

    @DeleteMapping("/{sojournId}")
    public void deleteSojourn(@PathVariable UUID sojournId) {
        if (!this.sojournRepository.existsById(sojournId)) {
            throw new RuntimeException("Sojourn not found with id: " + sojournId);
        }
        this.sojournRepository.deleteById(sojournId);
    }
}