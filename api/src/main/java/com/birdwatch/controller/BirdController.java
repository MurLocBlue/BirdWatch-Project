package com.birdwatch.controller;

import com.birdwatch.entity.Bird;
import com.birdwatch.dto.BirdDTO;
import com.birdwatch.service.BirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/birds")
public class BirdController {

    private BirdDTO convertToDTO(Bird bird) {
        return new BirdDTO(
            bird.getId(),
            bird.getName(),
            bird.getColor(),
            bird.getWeight(),
            bird.getHeight(),
            bird.getCreatedAt()
        );
    }

    @Autowired
    private BirdService birdService;

    @GetMapping
    public List<BirdDTO> getAllBirds() {
        return birdService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BirdDTO> getBirdById(@PathVariable Long id) {
        return birdService.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Bird createBird(@RequestBody Bird bird) {
        return birdService.save(bird);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bird> updateBird(@PathVariable Long id, @RequestBody Bird bird) {
        return birdService.findById(id)
                .map(existingBird -> {
                    bird.setId(id);
                    return ResponseEntity.ok(birdService.save(bird));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBird(@PathVariable Long id) {
        return birdService.findById(id)
                .map(bird -> {
                    birdService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 