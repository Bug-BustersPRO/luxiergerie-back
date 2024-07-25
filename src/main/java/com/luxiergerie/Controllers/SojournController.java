package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.SojournDTO;
import com.luxiergerie.Domain.Entity.Sojourn;
import com.luxiergerie.Domain.Mapper.SojournMapper;
import com.luxiergerie.Domain.Repository.SojournRepository;
import com.luxiergerie.Services.SojournService;
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
        Sojourn sojourn = this.sojournService.updateSojourn(sojournId, sojournDTO);
        return SojournMapper.toDTO(sojourn);
    }

    @DeleteMapping("/{sojournId}")
    public HttpStatus deleteSojourn(@PathVariable UUID sojournId) {
        this.sojournService.deleteSojourn(sojournId);
        return HttpStatus.NO_CONTENT;
    }

    @GetMapping("/{sojournId}/recover")
    public HttpStatus recoverPasswordAndIdentifier(@PathVariable UUID sojournId) {
        this.sojournService.recoverPasswordAndIdentifier(sojournId);
        return HttpStatus.OK;
    }

    @PutMapping("/{sojournId}/cancel")
    public SojournDTO cancelSojourn(@PathVariable UUID sojournId) {
        Sojourn sojourn = this.sojournService.cancelSojourn(sojournId);
        return SojournMapper.toDTO(sojourn);
    }
}