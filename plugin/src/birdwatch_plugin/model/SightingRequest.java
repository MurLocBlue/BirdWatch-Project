package birdwatch_plugin.model;

import java.time.LocalDateTime;

public class SightingRequest {
    private Long birdId;
    private String location;
    private LocalDateTime sightingDate;

    public SightingRequest() {
    }

    public SightingRequest(Long birdId, String location, LocalDateTime sightingDate) {
        this.birdId = birdId;
        this.location = location;
        this.sightingDate = sightingDate;
    }

    public Long getBirdId() {
        return birdId;
    }

    public void setBirdId(Long birdId) {
        this.birdId = birdId;
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
} 