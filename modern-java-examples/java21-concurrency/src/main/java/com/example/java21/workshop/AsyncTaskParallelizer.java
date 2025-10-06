package com.example.java21.workshop;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Illustration de la parallélisation de tâches asynchrones avec {@link CompletableFuture}.
 */
public final class AsyncTaskParallelizer {

    public static void main(String[] args) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            new AsyncTaskParallelizer(executor).run();
        }
    }

    private final ExecutorService executor;

    public AsyncTaskParallelizer(ExecutorService executor) {
        this.executor = executor;
    }

    private void run() {
        var start = Instant.now();
        var warehouses = List.of("Paris", "Lyon", "Lille", "Nice");

        var inventoryFuture = CompletableFuture.supplyAsync(() -> loadInventory(warehouses), executor);
        var shippingFuture = CompletableFuture.supplyAsync(() -> loadShippingTimes(warehouses), executor);
        var slaFuture = CompletableFuture.supplyAsync(this::loadSlaCommitments, executor);

        var all = CompletableFuture.allOf(inventoryFuture, shippingFuture, slaFuture)
                .orTimeout(2, TimeUnit.SECONDS)
                .exceptionally(ex -> {
                    log("Erreur globale : " + ex);
                    return null;
                });

        all.join();

        var decisions = inventoryFuture.join().entrySet().stream()
                .map(entry -> "%s → stock=%d, livraison=%s, SLA=%s"
                        .formatted(entry.getKey(),
                                entry.getValue(),
                                shippingFuture.join().get(entry.getKey()),
                                slaFuture.join().getOrDefault(entry.getKey(), "standard")))
                .toList();

        decisions.forEach(decision -> log("Décision : " + decision));
        log("Parallélisation terminée en %d ms".formatted(Duration.between(start, Instant.now()).toMillis()));
    }

    private Map<String, Integer> loadInventory(List<String> warehouses) {
        simulateDelay(300);
        return warehouses.stream().collect(Collectors.toMap(city -> city, city -> 50 + ThreadLocalRandom.current().nextInt(100)));
    }

    private Map<String, Duration> loadShippingTimes(List<String> warehouses) {
        simulateDelay(200);
        return warehouses.stream().collect(Collectors.toMap(city -> city, city -> Duration.ofHours(24 + ThreadLocalRandom.current().nextInt(48))));
    }

    private Map<String, String> loadSlaCommitments() {
        simulateDelay(150);
        return Map.of("Paris", "express", "Lyon", "prioritaire");
    }

    private void simulateDelay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void log(String message) {
        System.out.printf("[%s][%s] %s%n", Instant.now(), Thread.currentThread().getName(), message);
    }
}
