package com.birdwatch.service;

import com.birdwatch.entity.Bird;
import com.birdwatch.repository.BirdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing bird-related operations.
 * Handles business logic for bird CRUD operations and searching.
 */
@Service
public class BirdService {
    
    @Autowired
    private BirdRepository birdRepository;

    /**
     * Retrieves all birds from the database.
     *
     * @return A list of all birds
     */
    public List<Bird> findAll() {
        return birdRepository.findAll();
    }

    /**
     * Retrieves a specific bird by its ID.
     *
     * @param id The ID of the bird to retrieve
     * @return Optional containing the bird if found, empty if not found
     */
    public Optional<Bird> findById(Long id) {
        return birdRepository.findById(id);
    }

    /**
     * Saves a bird to the database.
     *
     * @param bird The bird to save
     * @return The saved bird entity
     */
    public Bird save(Bird bird) {
        return birdRepository.save(bird);
    }

    /**
     * Deletes a bird by its ID.
     *
     * @param id The ID of the bird to delete
     */
    public void deleteById(Long id) {
        birdRepository.deleteById(id);
    }

    /**
     * Searches for birds based on name and/or color.
     *
     * @param name Optional parameter to filter birds by name
     * @param color Optional parameter to filter birds by color
     * @return A list of matching birds
     */
    public List<Bird> searchBirds(String name, String color) {
        return birdRepository.searchBirds(name, color);
    }
} 