package com.birdwatch.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Entity class representing a bird sighting in the system.
 * Contains information about when and where a bird was sighted.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sightings")
public class Sighting {
    /**
     * Unique identifier for the sighting.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The bird that was sighted.
     * Must not be null and uses JSON back reference to prevent infinite recursion.
     */
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "bird_id", nullable = false)
    private Bird bird;

    /**
     * Location where the bird was sighted.
     * Must not be null and cannot exceed 100 characters.
     */
    @Column(nullable = false, length = 100)
    private String location;

    /**
     * Date and time when the bird was sighted.
     * Must not be null.
     */
    @Column(name = "sighting_date", nullable = false)
    private LocalDateTime sightingDate;

    /**
     * Timestamp when the sighting record was created.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}