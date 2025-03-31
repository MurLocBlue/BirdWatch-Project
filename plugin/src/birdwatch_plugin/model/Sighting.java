package birdwatch_plugin.model;

import java.time.LocalDateTime;

public class Sighting {
    private Long id;
    private String location;
    private LocalDateTime sightingDate;
    private LocalDateTime createdAt;
    private Bird bird;

    public Sighting() {
    }

    public Sighting(Long id, String location, LocalDateTime sightingDate, LocalDateTime createdAt, Bird bird) {
        this.id = id;
        this.location = location;
        this.sightingDate = sightingDate;
        this.createdAt = createdAt;
        this.bird = bird;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getSightingDate() {
        return sightingDate;
    }

    public void setSightingDate(LocalDateTime sightingDate) {
        this.sightingDate = sightingDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Bird getBird() {
        return bird;
    }

    public void setBird(Bird bird) {
        this.bird = bird;
    }
} 