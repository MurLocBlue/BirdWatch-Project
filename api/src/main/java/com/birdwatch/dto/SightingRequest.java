package com.birdwatch.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SightingRequest {
    private Long birdId;
    private String location;
    private LocalDateTime sightingDate;
} 