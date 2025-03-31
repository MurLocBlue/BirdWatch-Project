package com.birdwatch.controller;

import com.birdwatch.entity.Sighting;
import com.birdwatch.dto.SightingDTO;
import com.birdwatch.dto.BirdDTO;
import com.birdwatch.dto.SightingRequest;
import com.birdwatch.service.SightingService;
import com.birdwatch.service.BirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/sightings")
public class SightingController {

    private SightingDTO convertToDTO(Sighting sighting) {
        BirdDTO birdDTO = new BirdDTO(
            sighting.getBird().getId(),
            sighting.getBird().getName(),
            sighting.getBird().getColor(),
            sighting.getBird().getWeight(),
            sighting.getBird().getHeight(),
            sighting.getBird().getCreatedAt()
        );

        return new SightingDTO(
            sighting.getId(),
            sighting.getLocation(),
            sighting.getSightingDate(),
            sighting.getCreatedAt(),
            birdDTO
        );
    }

    @Autowired
    private SightingService sightingService;

    @Autowired
    private BirdService birdService;

    @GetMapping
    public List<SightingDTO> getAllSightings() {
        return sightingService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SightingDTO> getSightingById(@PathVariable Long id) {
        return sightingService.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SightingDTO> createSighting(@RequestBody SightingRequest request) {
        return birdService.findById(request.getBirdId())
            .map(bird -> {
                Sighting sighting = new Sighting();
                sighting.setBird(bird);
                sighting.setLocation(request.getLocation());
                sighting.setSightingDate(request.getSightingDate());
                return ResponseEntity.ok(convertToDTO(sightingService.save(sighting)));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SightingDTO> updateSighting(@PathVariable Long id, @RequestBody SightingRequest request) {
        return sightingService.findById(id)
            .flatMap(existingSighting -> 
                birdService.findById(request.getBirdId())
                    .map(bird -> {
                        existingSighting.setBird(bird);
                        existingSighting.setLocation(request.getLocation());
                        existingSighting.setSightingDate(request.getSightingDate());
                        return ResponseEntity.ok(convertToDTO(sightingService.save(existingSighting)));
                    })
            )
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSighting(@PathVariable Long id) {
        return sightingService.findById(id)
                .map(sighting -> {
                    sightingService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 