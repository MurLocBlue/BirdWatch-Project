package birdwatch_plugin.api;

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
import birdwatch_plugin.model.Bird;
import birdwatch_plugin.model.Sighting;

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

    public void shutdown() {
        executor.shutdown();
    }
} 