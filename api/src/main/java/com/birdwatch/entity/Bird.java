package com.birdwatch.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "birds")
public class Bird {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String color;

    @Column(nullable = false, precision = 5, scale = 2)
    private Double weight;

    @Column(nullable = false, precision = 5, scale = 2)
    private Double height;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "bird", cascade = CascadeType.ALL)
    private List<Sighting> sightings;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 