package com.birdwatch.repository;

import com.birdwatch.entity.Bird;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for Bird entities.
 * Provides database operations for Bird entities including custom search functionality.
 */
@Repository
public interface BirdRepository extends JpaRepository<Bird, Long> {
    /**
     * Searches for birds based on name and/or color using case-insensitive partial matching.
     * If a parameter is null, it will not be included in the search criteria.
     *
     * @param name Optional parameter to filter birds by name
     * @param color Optional parameter to filter birds by color
     * @return A list of birds matching the search criteria
     */
    @Query("SELECT b FROM Bird b WHERE " +
           "(:name IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:color IS NULL OR LOWER(b.color) LIKE LOWER(CONCAT('%', :color, '%')))")
    List<Bird> searchBirds(@Param("name") String name, @Param("color") String color);
} 