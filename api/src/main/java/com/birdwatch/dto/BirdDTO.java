package com.birdwatch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BirdDTO {
    private Long id;
    private String name;
    private String color;
    private Double weight;
    private Double height;
    private LocalDateTime createdAt;
} 