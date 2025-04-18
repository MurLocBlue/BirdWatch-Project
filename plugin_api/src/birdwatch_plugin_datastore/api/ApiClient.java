package birdwatch_plugin_datastore.api;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import birdwatch_plugin_datastore.model.Bird;
import birdwatch_plugin_datastore.model.Sighting;
import birdwatch_plugin_datastore.model.SightingRequest;

/**
 * Client class for interacting with the BirdWatch REST API.
 * Provides asynchronous methods for all API operations.
 *
 * @author Costin Marinescu
 * @version 0.1
 */
public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080/api";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final ExecutorService executor;

    /**
     * Constructs a new ApiClient with default configuration.
     * Initializes HTTP client, JSON mapper, and executor service.
     */
    public ApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Retrieves all birds from the API.
     *
     * @return CompletableFuture containing a list of all birds
     */
    public CompletableFuture<List<Bird>> getBirds() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/birds"))
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    try {
                        return objectMapper.readValue(response.body(), new TypeReference<List<Bird>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse birds response", e);
                    }
                });
    }

    /**
     * Creates a new bird in the system.
     *
     * @param bird The bird to create
     * @return CompletableFuture containing the created bird
     */
    public CompletableFuture<Bird> createBird(Bird bird) {
        try {
            String jsonBody = objectMapper.writeValueAsString(bird);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/birds"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        if (response.statusCode() >= 200 && response.statusCode() < 300) {
                            if (response.body() != null && !response.body().isEmpty()) {
                                try {
                                    return objectMapper.readValue(response.body(), Bird.class);
                                } catch (Exception e) {
                                    return bird;
                                }
                            } else {
                                return bird;
                            }
                        } else {
                            throw new RuntimeException("Server returned error status: " + response.statusCode());
                        }
                    });
        } catch (Exception e) {
            CompletableFuture<Bird> future = new CompletableFuture<>();
            future.completeExceptionally(new RuntimeException("Failed to create bird", e));
            return future;
        }
    }

    /**
     * Retrieves all sightings from the API.
     *
     * @return CompletableFuture containing a list of all sightings
     */
    public CompletableFuture<List<Sighting>> getSightings() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/sightings"))
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    try {
                        return objectMapper.readValue(response.body(), new TypeReference<List<Sighting>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse sightings response", e);
                    }
                });
    }

    /**
     * Creates a new sighting in the system.
     *
     * @param sighting The sighting to create
     * @return CompletableFuture containing the created sighting
     */
    public CompletableFuture<Sighting> createSighting(Sighting sighting) {
        try {
            SightingRequest request = new SightingRequest(
                sighting.getBird().getId(),
                sighting.getLocation(),
                sighting.getSightingDate()
            );

            String jsonBody = objectMapper.writeValueAsString(request);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/sightings"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        if (response.statusCode() >= 200 && response.statusCode() < 300) {
                            if (response.body() != null && !response.body().isEmpty()) {
                                try {
                                    return objectMapper.readValue(response.body(), Sighting.class);
                                } catch (Exception e) {
                                    return sighting;
                                }
                            } else {
                                return sighting;
                            }
                        } else {
                            throw new RuntimeException("Server returned error status: " + response.statusCode());
                        }
                    });
        } catch (Exception e) {
            CompletableFuture<Sighting> future = new CompletableFuture<>();
            future.completeExceptionally(new RuntimeException("Failed to create sighting", e));
            return future;
        }
    }

    /**
     * Searches for birds based on name and/or color.
     *
     * @param name Optional parameter to filter birds by name
     * @param color Optional parameter to filter birds by color
     * @return CompletableFuture containing a list of matching birds
     */
    public CompletableFuture<List<Bird>> searchBirds(String name, String color) {
        StringBuilder uriBuilder = new StringBuilder(BASE_URL + "/birds/search?");
        if (name != null && !name.isEmpty()) {
            uriBuilder.append("name=").append(name);
        }
        if (color != null && !color.isEmpty()) {
            if (uriBuilder.toString().endsWith("?")) {
                uriBuilder.append("color=").append(color);
            } else {
                uriBuilder.append("&color=").append(color);
            }
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriBuilder.toString()))
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    try {
                        return objectMapper.readValue(response.body(), new TypeReference<List<Bird>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse birds search response", e);
                    }
                });
    }

    /**
     * Searches for sightings based on bird name and/or location.
     *
     * @param birdName Optional parameter to filter sightings by bird name
     * @param location Optional parameter to filter sightings by location
     * @return CompletableFuture containing a list of matching sightings
     */
    public CompletableFuture<List<Sighting>> searchSightings(String birdName, String location, String startDate, String endDate) {
        StringBuilder uriBuilder = new StringBuilder(BASE_URL + "/sightings/search?");
        if (birdName != null && !birdName.isEmpty()) {
            uriBuilder.append("birdName=").append(birdName);
        }
        if (location != null && !location.isEmpty()) {
            if (uriBuilder.toString().endsWith("?")) {
                uriBuilder.append("location=").append(location);
            } else {
                uriBuilder.append("&location=").append(location);
            }
        }
        if (startDate != null && !startDate.isEmpty()) {
            if (uriBuilder.toString().endsWith("?")) {
                uriBuilder.append("startDate=").append(startDate);
            } else {
                uriBuilder.append("&startDate=").append(startDate);
            }
        }
        if (endDate != null && !endDate.isEmpty()) {
            if (uriBuilder.toString().endsWith("?")) {
                uriBuilder.append("endDate=").append(endDate);
            } else {
                uriBuilder.append("&endDate=").append(endDate);
            }
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriBuilder.toString()))
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    try {
                        return objectMapper.readValue(response.body(), new TypeReference<List<Sighting>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse sightings search response", e);
                    }
                });
    }

    /**
     * Deletes a bird by its ID.
     *
     * @param birdId The ID of the bird to delete
     * @return CompletableFuture that completes when the bird is deleted
     */
    public CompletableFuture<Void> deleteBird(Long birdId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/birds/" + birdId))
                .DELETE()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() >= 200 && response.statusCode() < 300) {
                        return null;
                    } else {
                        throw new RuntimeException("Server returned error status: " + response.statusCode());
                    }
                });
    }

    /**
     * Deletes a sighting by its ID.
     *
     * @param sightingId The ID of the sighting to delete
     * @return CompletableFuture that completes when the sighting is deleted
     */
    public CompletableFuture<Void> deleteSighting(Long sightingId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/sightings/" + sightingId))
                .DELETE()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() >= 200 && response.statusCode() < 300) {
                        return null;
                    } else {
                        throw new RuntimeException("Server returned error status: " + response.statusCode());
                    }
                });
    }

    /**
     * Shuts down the executor service.
     * Should be called when the client is no longer needed.
     */
    public void shutdown() {
        executor.shutdown();
    }
} 