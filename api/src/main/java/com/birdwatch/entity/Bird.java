package com.birdwatch.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Entity class representing a bird in the system.
 * Contains information about the bird's physical characteristics and its sightings.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "birds")
public class Bird {
    /**
     * Unique identifier for the bird.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the bird.
     * Must not be null and cannot exceed 100 characters.
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Color of the bird.
     * Must not be null and cannot exceed 50 characters.
     */
    @Column(nullable = false, length = 50)
    private String color;

    /**
     * Weight of the bird in kilograms.
     * Must not be null and can have up to 2 decimal places.
     */
    @Column(nullable = false, precision = 5, scale = 2)
    private Double weight;

    /**
     * Height of the bird in centimeters.
     * Must not be null and can have up to 2 decimal places.
     */
    @Column(nullable = false, precision = 5, scale = 2)
    private Double height;

    /**
     * Timestamp when the bird record was created.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * List of sightings associated with this bird.
     * Uses JSON managed reference to prevent infinite recursion in JSON serialization.
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "bird", cascade = CascadeType.ALL)
    private List<Sighting> sightings;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 