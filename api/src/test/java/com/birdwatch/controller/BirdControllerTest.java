package com.birdwatch.controller;

import com.birdwatch.entity.Bird;
import com.birdwatch.dto.BirdDTO;
import com.birdwatch.service.BirdService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the BirdController.
 * Tests all REST endpoints related to bird operations including CRUD operations.
 * Uses Mockito for mocking the service layer dependencies and JUnit Jupiter for testing.
 * This class demonstrates the use of @ExtendWith(MockitoExtension.class) for integration with Mockito.
 */
@ExtendWith(MockitoExtension.class)
class BirdControllerTest {

    @Mock
    private BirdService birdService;

    @InjectMocks
    private BirdController birdController;

    private Bird testBird;
    private Bird testBird2;

    /**
     * Sets up test data before each test method.
     * Creates two test Bird instances with different properties
     * to be used across various test methods.
     * This ensures each test starts with a fresh set of test data.
     */
    @BeforeEach
    void setUp() {
        testBird = new Bird();
        testBird.setId(1L);
        testBird.setName("Test Bird");
        testBird.setColor("Blue");
        testBird.setWeight(1.5);
        testBird.setHeight(0.3);
        testBird.setCreatedAt(LocalDateTime.now());

        testBird2 = new Bird();
        testBird2.setId(2L);
        testBird2.setName("Test Bird 2");
        testBird2.setColor("Red");
        testBird2.setWeight(2.0);
        testBird2.setHeight(0.4);
        testBird2.setCreatedAt(LocalDateTime.now());
    }

    /**
     * Tests the GET /api/birds endpoint.
     * Verifies that the endpoint returns a list of all birds with correct data.
     * Checks that the service method is called exactly once and the returned list
     * contains the expected number of birds.
     */
    @Test
    void getAllBirds_ShouldReturnListOfBirds() {
        // Arrange
        when(birdService.findAll()).thenReturn(Arrays.asList(testBird, testBird2));

        // Act
        List<BirdDTO> result = birdController.getAllBirds();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(birdService, times(1)).findAll();
    }

    /**
     * Tests the GET /api/birds/{id} endpoint for an existing bird.
     * Verifies that the endpoint returns the correct bird when it exists.
     * Checks both the response status and the returned bird's properties.
     */
    @SuppressWarnings("null")
    @Test
    void getBirdById_WhenBirdExists_ShouldReturnBird() {
        // Arrange
        when(birdService.findById(1L)).thenReturn(Optional.of(testBird));

        // Act
        ResponseEntity<BirdDTO> response = birdController.getBirdById(1L);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(testBird.getId(), response.getBody().getId());
        verify(birdService, times(1)).findById(1L);
    }

    /**
     * Tests the GET /api/birds/{id} endpoint for a non-existing bird.
     * Verifies that the endpoint returns a 404 NOT_FOUND status when
     * the requested bird doesn't exist in the database.
     */
    @Test
    void getBirdById_WhenBirdDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        when(birdService.findById(999L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<BirdDTO> response = birdController.getBirdById(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(birdService, times(1)).findById(999L);
    }

    /**
     * Tests the POST /api/birds endpoint.
     * Verifies that a new bird is created successfully with the correct data.
     * Checks that the service's save method is called and the returned bird
     * matches the input data.
     */
    @Test
    void createBird_ShouldReturnCreatedBird() {
        // Arrange
        when(birdService.save(any(Bird.class))).thenReturn(testBird);

        // Act
        Bird result = birdController.createBird(testBird);

        // Assert
        assertNotNull(result);
        assertEquals(testBird.getId(), result.getId());
        assertEquals(testBird.getName(), result.getName());
        verify(birdService, times(1)).save(any(Bird.class));
    }

    /**
     * Tests the PUT /api/birds/{id} endpoint for an existing bird.
     * Verifies that a bird is updated successfully when it exists.
     * Checks that both findById and save methods are called on the service
     * and the returned bird matches the updated data.
     */
    @SuppressWarnings("null")
    @Test
    void updateBird_WhenBirdExists_ShouldReturnUpdatedBird() {
        // Arrange
        when(birdService.findById(1L)).thenReturn(Optional.of(testBird));
        when(birdService.save(any(Bird.class))).thenReturn(testBird);

        // Act
        ResponseEntity<Bird> response = birdController.updateBird(1L, testBird);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(testBird.getId(), response.getBody().getId());
        verify(birdService, times(1)).findById(1L);
        verify(birdService, times(1)).save(any(Bird.class));
    }

    /**
     * Tests the DELETE /api/birds/{id} endpoint for an existing bird.
     * Verifies that a bird is deleted successfully when it exists.
     * Checks that both findById and deleteById methods are called on the service
     * and the response status indicates success.
     */
    @Test
    void deleteBird_WhenBirdExists_ShouldReturnOk() {
        // Arrange
        when(birdService.findById(1L)).thenReturn(Optional.of(testBird));
        doNothing().when(birdService).deleteById(1L);

        // Act
        ResponseEntity<Void> response = birdController.deleteBird(1L);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        verify(birdService, times(1)).findById(1L);
        verify(birdService, times(1)).deleteById(1L);
    }
} 