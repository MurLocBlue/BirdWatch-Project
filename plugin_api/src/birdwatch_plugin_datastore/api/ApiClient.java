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

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080/api";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final ExecutorService executor;

    public ApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.executor = Executors.newSingleThreadExecutor();
    }

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

    public CompletableFuture<List<Sighting>> searchSightings(String birdName, String location) {
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

    public void shutdown() {
        executor.shutdown();
    }
} 