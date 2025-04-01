package birdwatch_plugin_datastore.model;

import java.time.LocalDateTime;

/**
 * Model class representing a bird sighting in the BirdWatch system.
 * Contains information about when and where a bird was sighted.
 *
 * @author Costin Marinescu
 * @version 0.1
 */
public class Sighting {
    private Long id;
    private String location;
    private LocalDateTime sightingDate;
    private LocalDateTime createdAt;
    private Bird bird;

    public Sighting() {
    }

    /**
     * Constructor with all fields.
     *
     * @param id The unique identifier of the sighting
     * @param location The location where the bird was sighted
     * @param sightingDate The date and time when the bird was sighted
     * @param createdAt The timestamp when the sighting was created
     * @param bird The bird that was sighted
     */
    public Sighting(Long id, String location, LocalDateTime sightingDate, LocalDateTime createdAt, Bird bird) {
        this.id = id;
        this.location = location;
        this.sightingDate = sightingDate;
        this.createdAt = createdAt;
        this.bird = bird;
    }

    /**
     * Gets the unique identifier of the sighting.
     *
     * @return The sighting's ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the sighting.
     *
     * @param id The sighting's ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the location where the bird was sighted.
     *
     * @return The sighting location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location where the bird was sighted.
     *
     * @param location The sighting location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the date and time when the bird was sighted.
     *
     * @return The sighting date and time
     */
    public LocalDateTime getSightingDate() {
        return sightingDate;
    }

    /**
     * Sets the date and time when the bird was sighted.
     *
     * @param sightingDate The sighting date and time
     */
    public void setSightingDate(LocalDateTime sightingDate) {
        this.sightingDate = sightingDate;
    }

    /**
     * Gets the creation timestamp of the sighting.
     *
     * @return The timestamp when the sighting was created
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of the sighting.
     *
     * @param createdAt The timestamp when the sighting was created
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the bird that was sighted.
     *
     * @return The sighted bird
     */
    public Bird getBird() {
        return bird;
    }

    /**
     * Sets the bird that was sighted.
     *
     * @param bird The sighted bird
     */
    public void setBird(Bird bird) {
        this.bird = bird;
    }
} 