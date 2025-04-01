package com.birdwatch.service;

import com.birdwatch.entity.Sighting;
import com.birdwatch.repository.SightingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SightingService {
    
    @Autowired
    private SightingRepository sightingRepository;

    public List<Sighting> findAll() {
        return sightingRepository.findAll();
    }

    public Optional<Sighting> findById(Long id) {
        return sightingRepository.findById(id);
    }

    public Sighting save(Sighting sighting) {
        return sightingRepository.save(sighting);
    }

    public void deleteById(Long id) {
        sightingRepository.deleteById(id);
    }

    public List<Sighting> searchSightings(String birdName, String location) {
        return sightingRepository.searchSightings(birdName, location);
    }
} 