package com.birdwatch.repository;

import com.birdwatch.entity.Bird;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BirdRepository extends JpaRepository<Bird, Long> {
    @Query("SELECT b FROM Bird b WHERE " +
           "(:name IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:color IS NULL OR LOWER(b.color) LIKE LOWER(CONCAT('%', :color, '%')))")
    List<Bird> searchBirds(@Param("name") String name, @Param("color") String color);
} 