package com.birdwatch.controller;

import com.birdwatch.entity.Sighting;
import com.birdwatch.dto.SightingDTO;
import com.birdwatch.dto.BirdDTO;
import com.birdwatch.dto.SightingRequest;
import com.birdwatch.service.SightingService;
import com.birdwatch.service.BirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

/**
 * REST controller for managing bird sighting operations.
 * Provides endpoints for CRUD operations and searching bird sightings.
 */
@RestController
@RequestMapping("/api/sightings")
public class SightingController {

    /**
     * Converts a Sighting entity to a SightingDTO.
     *
     * @param sighting The sighting entity to convert
     * @return A new SightingDTO containing the sighting's data
     */
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

    /**
     * Retrieves all bird sightings in the system.
     *
     * @return A list of all sightings as DTOs
     */
    @GetMapping
    public List<SightingDTO> getAllSightings() {
        return sightingService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Searches for bird sightings based on bird name and/or location.
     *
     * @param birdName Optional parameter to filter sightings by bird name
     * @param location Optional parameter to filter sightings by location
     * @return A list of matching sightings as DTOs
     */
    @GetMapping("/search")
    public List<SightingDTO> searchSightings(
            @RequestParam(required = false) String birdName,
            @RequestParam(required = false) String location) {
        
        return sightingService.searchSightings(birdName, location).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific sighting by its ID.
     *
     * @param id The ID of the sighting to retrieve
     * @return ResponseEntity containing the sighting DTO if found, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<SightingDTO> getSightingById(@PathVariable Long id) {
        return sightingService.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new bird sighting.
     *
     * @param request The sighting request containing bird ID, location, and sighting date
     * @return ResponseEntity containing the created sighting DTO if successful, or 404 if bird not found
     */
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

    /**
     * Updates an existing bird sighting.
     *
     * @param id The ID of the sighting to update
     * @param request The updated sighting data
     * @return ResponseEntity containing the updated sighting DTO if found, or 404 if not found
     */
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

    /**
     * Deletes a bird sighting by its ID.
     *
     * @param id The ID of the sighting to delete
     * @return ResponseEntity with status 200 if deleted, or 404 if not found
     */
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