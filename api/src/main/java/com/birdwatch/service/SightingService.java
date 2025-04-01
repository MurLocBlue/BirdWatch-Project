package com.birdwatch.service;

import com.birdwatch.entity.Sighting;
import com.birdwatch.repository.SightingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing bird sighting operations.
 * Handles business logic for sighting CRUD operations and searching.
 */
@Service
public class SightingService {
    
    @Autowired
    private SightingRepository sightingRepository;

    /**
     * Retrieves all bird sightings from the database.
     *
     * @return A list of all sightings
     */
    public List<Sighting> findAll() {
        return sightingRepository.findAll();
    }

    /**
     * Retrieves a specific sighting by its ID.
     *
     * @param id The ID of the sighting to retrieve
     * @return Optional containing the sighting if found, empty if not found
     */
    public Optional<Sighting> findById(Long id) {
        return sightingRepository.findById(id);
    }

    /**
     * Saves a sighting to the database.
     *
     * @param sighting The sighting to save
     * @return The saved sighting entity
     */
    public Sighting save(Sighting sighting) {
        return sightingRepository.save(sighting);
    }

    /**
     * Deletes a sighting by its ID.
     *
     * @param id The ID of the sighting to delete
     */
    public void deleteById(Long id) {
        sightingRepository.deleteById(id);
    }

    /**
     * Searches for sightings based on bird name and/or location.
     *
     * @param birdName Optional parameter to filter sightings by bird name
     * @param location Optional parameter to filter sightings by location
     * @return A list of matching sightings
     */
    public List<Sighting> searchSightings(String birdName, String location) {
        return sightingRepository.searchSightings(birdName, location);
    }
} 