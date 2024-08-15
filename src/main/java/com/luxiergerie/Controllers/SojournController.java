package com.luxiergerie.Controllers;

import com.luxiergerie.DTO.SojournDTO;
import com.luxiergerie.Mapper.SojournMapper;
import com.luxiergerie.Model.Entity.Sojourn;
import com.luxiergerie.Repository.SojournRepository;
import com.luxiergerie.Services.SojournService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.luxiergerie.Mapper.SojournMapper.MappedSojournFrom;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

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
                .map(SojournMapper::MappedSojournFrom)
                .collect(toList());
    }

    @PostMapping
    public HttpStatus createSojourn(@RequestBody SojournDTO sojournDTO) {
        this.sojournService.createSojourn(sojournDTO);
        return CREATED;
    }

    @PutMapping("/{sojournId}")
    public SojournDTO updateSojourn(@PathVariable UUID sojournId, @RequestBody SojournDTO sojournDTO) {
        Sojourn sojourn = this.sojournService.updateSojourn(sojournId, sojournDTO);
        return MappedSojournFrom(sojourn);
    }

    @DeleteMapping("/{sojournId}")
    public HttpStatus deleteSojourn(@PathVariable UUID sojournId) {
        this.sojournService.deleteSojourn(sojournId);
        return NO_CONTENT;
    }

    @GetMapping("/{sojournId}/recover")
    public HttpStatus recoverPasswordAndIdentifier(@PathVariable UUID sojournId) {
        this.sojournService.recoverPasswordAndIdentifier(sojournId);
        return OK;
    }

    @PutMapping("/{sojournId}/cancel")
    public SojournDTO cancelSojourn(@PathVariable UUID sojournId) {
        Sojourn sojourn = this.sojournService.cancelSojourn(sojournId);
        return MappedSojournFrom(sojourn);
    }

}