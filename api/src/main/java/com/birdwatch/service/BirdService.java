package com.birdwatch.service;

import com.birdwatch.entity.Bird;
import com.birdwatch.repository.BirdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BirdService {
    
    @Autowired
    private BirdRepository birdRepository;

    public List<Bird> findAll() {
        return birdRepository.findAll();
    }

    public Optional<Bird> findById(Long id) {
        return birdRepository.findById(id);
    }

    public Bird save(Bird bird) {
        return birdRepository.save(bird);
    }

    public void deleteById(Long id) {
        birdRepository.deleteById(id);
    }

    public List<Bird> searchBirds(String name, String color) {
        return birdRepository.searchBirds(name, color);
    }
} 