package com.birdwatch.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Request DTO for creating or updating bird sightings.
 * Contains the necessary data to create or update a sighting record.
 */
@Data
public class SightingRequest {
    /**
     * ID of the bird that was sighted.
     */
    private Long birdId;

    /**
     * Location where the bird was sighted.
     */
    private String location;

    /**
     * Date and time when the bird was sighted.
     */
    private LocalDateTime sightingDate;
} 