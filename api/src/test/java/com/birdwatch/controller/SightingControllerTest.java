package com.birdwatch.controller;

import com.birdwatch.entity.Bird;
import com.birdwatch.entity.Sighting;
import com.birdwatch.dto.SightingDTO;
import com.birdwatch.dto.BirdDTO;
import com.birdwatch.dto.SightingRequest;
import com.birdwatch.service.SightingService;
import com.birdwatch.service.BirdService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SightingController.
 * Tests all REST endpoints related to bird sightings including CRUD operations and search functionality.
 * Uses Spring's MockMvc for simulating HTTP requests and MockBean for service layer dependencies.
 */
@WebMvcTest(SightingController.class)
public class SightingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SightingService sightingService;

    @MockBean
    private BirdService birdService;

    @Autowired
    private ObjectMapper objectMapper;

    private Bird testBird;
    private Sighting testSighting;
    @SuppressWarnings("unused")
    private SightingDTO testSightingDTO;
    private SightingRequest testSightingRequest;

    /**
     * Sets up test data before each test method.
     * Creates test instances of Bird, Sighting, SightingDTO, and SightingRequest
     * with predefined values for use in test methods.
     */
    @BeforeEach
    void setUp() {
        testBird = new Bird();
        testBird.setId(1L);
        testBird.setName("Test Bird");
        testBird.setColor("Blue");
        testBird.setHeight(10.0);
        testBird.setWeight(1.0);
        testBird.setCreatedAt(LocalDateTime.now());

        testSighting = new Sighting();
        testSighting.setId(1L);
        testSighting.setBird(testBird);
        testSighting.setLocation("Test Location");
        testSighting.setSightingDate(LocalDateTime.now());
        testSighting.setCreatedAt(LocalDateTime.now());

        testSightingDTO = new SightingDTO(
            testSighting.getId(),
            testSighting.getLocation(),
            testSighting.getSightingDate(),
            testSighting.getCreatedAt(),
            new BirdDTO(
                testBird.getId(),
                testBird.getName(),
                testBird.getColor(),
                testBird.getWeight(),
                testBird.getHeight(),
                testBird.getCreatedAt()
            )
        );

        testSightingRequest = new SightingRequest();
        testSightingRequest.setBirdId(testBird.getId());
        testSightingRequest.setLocation(testSighting.getLocation());
        testSightingRequest.setSightingDate(testSighting.getSightingDate());
    }

    /**
     * Tests the GET /api/sightings endpoint.
     * Verifies that the endpoint returns a list of all sightings with correct data.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void getAllSightings_ShouldReturnListOfSightings() throws Exception {
        when(sightingService.findAll()).thenReturn(Arrays.asList(testSighting));

        mockMvc.perform(get("/api/sightings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(testSighting.getId()))
                .andExpect(jsonPath("$[0].location").value(testSighting.getLocation()))
                .andExpect(jsonPath("$[0].bird.id").value(testBird.getId()));
    }

    /**
     * Tests the GET /api/sightings/search endpoint.
     * Verifies that the endpoint returns sightings matching the search criteria.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void searchSightings_ShouldReturnMatchingSightings() throws Exception {
        when(sightingService.searchSightings("Test", "Location"))
                .thenReturn(Arrays.asList(testSighting));

        mockMvc.perform(get("/api/sightings/search")
                .param("birdName", "Test")
                .param("location", "Location"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(testSighting.getId()));
    }

    /**
     * Tests the GET /api/sightings/search endpoint with a valid date range.
     * Verifies that sightings within the specified date range are returned correctly.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void searchSightings_WithValidDateRange_ShouldReturnMatchingSightings() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        testSighting.setSightingDate(now);
        
        when(sightingService.searchSightings(null, null))
                .thenReturn(Arrays.asList(testSighting));

        mockMvc.perform(get("/api/sightings/search")
                .param("startDate", now.minusHours(1).format(DateTimeFormatter.ISO_DATE_TIME))
                .param("endDate", now.plusHours(1).format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(testSighting.getId()));
    }

    /**
     * Tests the GET /api/sightings/search endpoint with a date range that doesn't include any sightings.
     * Verifies that an empty list is returned when no sightings match the date criteria.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void searchSightings_WithDateOutsideRange_ShouldReturnEmptyList() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        testSighting.setSightingDate(now);
        
        when(sightingService.searchSightings(null, null))
                .thenReturn(Arrays.asList(testSighting));

        mockMvc.perform(get("/api/sightings/search")
                .param("startDate", now.plusHours(1).format(DateTimeFormatter.ISO_DATE_TIME))
                .param("endDate", now.plusHours(2).format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());
    }

    /**
     * Tests the GET /api/sightings/search endpoint with an invalid date format.
     * Verifies that the endpoint returns a 400 Bad Request status when the date format is invalid.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void searchSightings_WithInvalidDateFormat_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/sightings/search")
                .param("startDate", "invalid-date"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests the GET /api/sightings/search endpoint with input containing invalid characters.
     * Verifies that the endpoint returns a 400 Bad Request status when the input contains
     * characters not allowed by the sanitization rules.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void searchSightings_WithInvalidInputCharacters_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/sightings/search")
                .param("birdName", "Test@Bird")
                .param("location", "New York"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests the GET /api/sightings/search endpoint with input exceeding the maximum length.
     * Verifies that the endpoint returns a 400 Bad Request status when the input length
     * exceeds the maximum allowed length.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void searchSightings_WithInputTooLong_ShouldReturnBadRequest() throws Exception {
        String longInput = "a".repeat(101); // 101 characters
        mockMvc.perform(get("/api/sightings/search")
                .param("birdName", longInput))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests the GET /api/sightings/search endpoint with all search parameters.
     * Verifies that the endpoint correctly filters sightings based on bird name,
     * location, and date range when all parameters are provided.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void searchSightings_WithAllParameters_ShouldReturnMatchingSightings() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        testSighting.setSightingDate(now);
        
        when(sightingService.searchSightings("Test", "Location"))
                .thenReturn(Arrays.asList(testSighting));

        mockMvc.perform(get("/api/sightings/search")
                .param("birdName", "Test")
                .param("location", "Location")
                .param("startDate", now.minusHours(1).format(DateTimeFormatter.ISO_DATE_TIME))
                .param("endDate", now.plusHours(1).format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(testSighting.getId()));
    }

    /**
     * Tests the GET /api/sightings/{id} endpoint for an existing sighting.
     * Verifies that the endpoint returns the correct sighting when it exists.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void getSightingById_WhenSightingExists_ShouldReturnSighting() throws Exception {
        when(sightingService.findById(1L)).thenReturn(Optional.of(testSighting));

        mockMvc.perform(get("/api/sightings/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testSighting.getId()))
                .andExpect(jsonPath("$.location").value(testSighting.getLocation()));
    }

    /**
     * Tests the GET /api/sightings/{id} endpoint for a non-existing sighting.
     * Verifies that the endpoint returns a 404 status when the sighting doesn't exist.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void getSightingById_WhenSightingDoesNotExist_ShouldReturn404() throws Exception {
        when(sightingService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/sightings/1"))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests the POST /api/sightings endpoint when the referenced bird exists.
     * Verifies that a new sighting is created successfully with the correct data.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void createSighting_WhenBirdExists_ShouldCreateSighting() throws Exception {
        when(birdService.findById(testBird.getId())).thenReturn(Optional.of(testBird));
        when(sightingService.save(any(Sighting.class))).thenReturn(testSighting);

        mockMvc.perform(post("/api/sightings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSightingRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testSighting.getId()))
                .andExpect(jsonPath("$.location").value(testSighting.getLocation()));
    }

    /**
     * Tests the POST /api/sightings endpoint when the referenced bird doesn't exist.
     * Verifies that the endpoint returns a 404 status when attempting to create
     * a sighting for a non-existing bird.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void createSighting_WhenBirdDoesNotExist_ShouldReturn404() throws Exception {
        when(birdService.findById(testBird.getId())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/sightings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSightingRequest)))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests the PUT /api/sightings/{id} endpoint when both sighting and bird exist.
     * Verifies that the sighting is updated successfully with the correct data.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void updateSighting_WhenSightingAndBirdExist_ShouldUpdateSighting() throws Exception {
        when(sightingService.findById(1L)).thenReturn(Optional.of(testSighting));
        when(birdService.findById(testBird.getId())).thenReturn(Optional.of(testBird));
        when(sightingService.save(any(Sighting.class))).thenReturn(testSighting);

        mockMvc.perform(put("/api/sightings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSightingRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testSighting.getId()))
                .andExpect(jsonPath("$.location").value(testSighting.getLocation()));
    }

    /**
     * Tests the PUT /api/sightings/{id} endpoint when the sighting doesn't exist.
     * Verifies that the endpoint returns a 404 status when attempting to update
     * a non-existing sighting.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void updateSighting_WhenSightingDoesNotExist_ShouldReturn404() throws Exception {
        when(sightingService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/sightings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSightingRequest)))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests the DELETE /api/sightings/{id} endpoint for an existing sighting.
     * Verifies that the sighting is deleted successfully.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void deleteSighting_WhenSightingExists_ShouldDeleteSighting() throws Exception {
        when(sightingService.findById(1L)).thenReturn(Optional.of(testSighting));

        mockMvc.perform(delete("/api/sightings/1"))
                .andExpect(status().isOk());
    }

    /**
     * Tests the DELETE /api/sightings/{id} endpoint for a non-existing sighting.
     * Verifies that the endpoint returns a 404 status when attempting to delete
     * a non-existing sighting.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void deleteSighting_WhenSightingDoesNotExist_ShouldReturn404() throws Exception {
        when(sightingService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/sightings/1"))
                .andExpect(status().isNotFound());
    }
} 