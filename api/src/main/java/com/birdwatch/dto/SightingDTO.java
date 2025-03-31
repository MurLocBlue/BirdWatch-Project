package com.birdwatch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SightingDTO {
    private Long id;
    private String location;
    private LocalDateTime sightingDate;
    private LocalDateTime createdAt;
    private BirdDTO bird;
} 