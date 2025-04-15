package com.birdwatch.repository;

import com.birdwatch.entity.Sighting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for Sighting entities.
 * Provides database operations for Sighting entities including custom search functionality.
 */
@Repository
public interface SightingRepository extends JpaRepository<Sighting, Long> {
    /**
     * Searches for sightings based on bird name and/or location using case-insensitive partial matching.
     * If a parameter is null, it will not be included in the search criteria.
     *
     * @param birdName Optional parameter to filter sightings by bird name
     * @param location Optional parameter to filter sightings by location
     * @return A list of sightings matching the search criteria
     */
    @Query("SELECT s FROM Sighting s WHERE " +
           "(:birdName IS NULL OR LOWER(s.bird.name) LIKE LOWER(CONCAT('%', :birdName, '%'))) AND " +
           "(:location IS NULL OR LOWER(s.location) LIKE LOWER(CONCAT('%', :location, '%')))")
    List<Sighting> searchSightings(
            @Param("birdName") String birdName,
            @Param("location") String location
       );
} 