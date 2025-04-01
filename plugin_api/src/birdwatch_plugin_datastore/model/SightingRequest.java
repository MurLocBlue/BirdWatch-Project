package birdwatch_plugin_datastore.model;

import java.time.LocalDateTime;

/**
 * Request model class for creating or updating bird sightings.
 * Contains the necessary data to create or update a sighting record.
 *
 * @author Costin Marinescu
 * @version 0.1
 */
public class SightingRequest {
    private Long birdId;
    private String location;
    private LocalDateTime sightingDate;

    public SightingRequest() {
    }

    /**
     * Constructor with all fields.
     *
     * @param birdId The ID of the bird that was sighted
     * @param location The location where the bird was sighted
     * @param sightingDate The date and time when the bird was sighted
     */
    public SightingRequest(Long birdId, String location, LocalDateTime sightingDate) {
        this.birdId = birdId;
        this.location = location;
        this.sightingDate = sightingDate;
    }

    /**
     * Gets the ID of the bird that was sighted.
     *
     * @return The bird's ID
     */
    public Long getBirdId() {
        return birdId;
    }

    /**
     * Sets the ID of the bird that was sighted.
     *
     * @param birdId The bird's ID
     */
    public void setBirdId(Long birdId) {
        this.birdId = birdId;
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
} 