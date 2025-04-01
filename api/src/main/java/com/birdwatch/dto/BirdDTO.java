package com.birdwatch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Bird entities.
 * Used to transfer bird data between the API and clients.
 */
@Data
@AllArgsConstructor
public class BirdDTO {
    /**
     * Unique identifier for the bird.
     */
    private Long id;

    /**
     * Name of the bird.
     */
    private String name;

    /**
     * Color of the bird.
     */
    private String color;

    /**
     * Weight of the bird in kilograms.
     */
    private Double weight;

    /**
     * Height of the bird in centimeters.
     */
    private Double height;

    /**
     * Timestamp when the bird record was created.
     */
    private LocalDateTime createdAt;
} 