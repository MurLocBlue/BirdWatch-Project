package com.birdwatch.controller;

import com.birdwatch.entity.Bird;
import com.birdwatch.dto.BirdDTO;
import com.birdwatch.service.BirdService;
import com.birdwatch.utils.InputSanitizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing bird-related operations.
 * Provides endpoints for CRUD operations and searching birds.
 */
@RestController
@RequestMapping("/api/birds")
public class BirdController {

    /**
     * Converts a Bird entity to a BirdDTO.
     *
     * @param bird The bird entity to convert
     * @return A new BirdDTO containing the bird's data
     */
    private BirdDTO convertToDTO(Bird bird) {
        return new BirdDTO(
            bird.getId(),
            bird.getName(),
            bird.getColor(),
            bird.getWeight(),
            bird.getHeight(),
            bird.getCreatedAt()
        );
    }

    @Autowired
    private BirdService birdService;

    /**
     * Retrieves all birds in the system.
     *
     * @return A list of all birds as DTOs
     */
    @GetMapping
    public List<BirdDTO> getAllBirds() {
        return birdService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Searches for birds based on name and/or color.
     *
     * @param name Optional parameter to filter birds by name
     * @param color Optional parameter to filter birds by color
     * @return A list of matching birds as DTOs
     */
    @GetMapping("/search")
    public List<BirdDTO> searchBirds(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String color) {
            
        // Sanitize string inputs
        try {
            name = InputSanitizer.sanitizeInput(name);
            color = InputSanitizer.sanitizeInput(color);
        } catch (ResponseStatusException e) {
            throw e;
        }

        return birdService.searchBirds(name, color).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific bird by its ID.
     *
     * @param id The ID of the bird to retrieve
     * @return ResponseEntity containing the bird DTO if found, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<BirdDTO> getBirdById(@PathVariable Long id) {
        return birdService.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new bird.
     *
     * @param bird The bird entity to create
     * @return The created bird entity
     */
    @PostMapping
    public Bird createBird(@RequestBody Bird bird) {
        return birdService.save(bird);
    }

    /**
     * Updates an existing bird.
     *
     * @param id The ID of the bird to update
     * @param bird The updated bird data
     * @return ResponseEntity containing the updated bird if found, or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Bird> updateBird(@PathVariable Long id, @RequestBody Bird bird) {
        return birdService.findById(id)
                .map(existingBird -> {
                    bird.setId(id);
                    return ResponseEntity.ok(birdService.save(bird));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a bird by its ID.
     *
     * @param id The ID of the bird to delete
     * @return ResponseEntity with status 200 if deleted, or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBird(@PathVariable Long id) {
        return birdService.findById(id)
                .map(bird -> {
                    birdService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 