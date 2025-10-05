package com.app.reco;

import com.app.catalog.CatalogService;
import com.app.catalog.Media;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public final class RecommendationService {

    private final CatalogService catalogService;
    private final HttpClient httpClient;
    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    public RecommendationService(CatalogService catalogService, HttpClient httpClient) {
        this.catalogService = catalogService;
        this.httpClient = httpClient;
    }

    public CompletableFuture<List<Media>> recomander() {
        CompletableFuture<List<Media>> local = CompletableFuture.supplyAsync(catalogService::listAll, executor);
        CompletableFuture<List<Media>> distant = appelerApiDistant();

        return local.thenCombine(distant, (locaux, distants) -> Stream.concat(locaux.stream(), distants.stream())
                .distinct()
                .toList());
    }

    private CompletableFuture<List<Media>> appelerApiDistant() {
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://api.example.com/reco"))
                .timeout(Duration.ofSeconds(1))
                .GET()
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .exceptionally(ex -> "")
                .thenApply(this::parseRemote);
    }

    private List<Media> parseRemote(String body) {
        if (body == null || body.isBlank()) {
            return List.of();
        }
        return body.lines()
                .map(line -> line.split("\\|"))
                .filter(parts -> parts.length >= 2)
                .map(parts -> (Media) new com.app.catalog.Book(parts[0], parts[1], 250))
                .toList();
    }
}
