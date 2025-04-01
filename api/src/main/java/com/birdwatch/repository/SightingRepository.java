package com.birdwatch.repository;

import com.birdwatch.entity.Sighting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SightingRepository extends JpaRepository<Sighting, Long> {
    @Query("SELECT s FROM Sighting s WHERE " +
           "(:birdName IS NULL OR LOWER(s.bird.name) LIKE LOWER(CONCAT('%', :birdName, '%'))) AND " +
           "(:location IS NULL OR LOWER(s.location) LIKE LOWER(CONCAT('%', :location, '%')))")
    List<Sighting> searchSightings(
            @Param("birdName") String birdName,
            @Param("location") String location);
} 