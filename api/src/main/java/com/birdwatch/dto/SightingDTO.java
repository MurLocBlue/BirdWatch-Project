package com.birdwatch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Sighting entities.
 * Used to transfer sighting data between the API and clients.
 */
@Data
@AllArgsConstructor
public class SightingDTO {
    /**
     * Unique identifier for the sighting.
     */
    private Long id;

    /**
     * Location where the bird was sighted.
     */
    private String location;

    /**
     * Date and time when the bird was sighted.
     */
    private LocalDateTime sightingDate;

    /**
     * Timestamp when the sighting record was created.
     */
    private LocalDateTime createdAt;

    /**
     * The bird that was sighted.
     */
    private BirdDTO bird;
} 