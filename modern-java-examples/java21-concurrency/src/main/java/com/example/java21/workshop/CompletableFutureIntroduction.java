package com.example.java21.workshop;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Introduction aux {@link CompletableFuture} exploitant les ajouts de Java 9+.
 */
public final class CompletableFutureIntroduction {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var start = Instant.now();

            var temperatureFuture = CompletableFuture.supplyAsync(() -> loadTemperature("Paris"), executor)
                    .orTimeout(500, TimeUnit.MILLISECONDS) // Java 9 : timeout direct
                    .completeOnTimeout(18.0, 600, TimeUnit.MILLISECONDS); // Java 9 : valeur par défaut

            var humidityFuture = CompletableFuture.supplyAsync(() -> loadHumidity("Paris"), executor)
                    .orTimeout(500, TimeUnit.MILLISECONDS)
                    .exceptionally(ex -> {
                        log("Humidité indisponible : " + ex.getMessage());
                        return 55.0;
                    });

            var comfortFuture = temperatureFuture.thenCombine(humidityFuture, CompletableFutureIntroduction::computeComfortIndex)
                    .thenApply(index -> Math.round(index * 100.0) / 100.0)
                    .whenComplete((value, error) -> {
                        if (error != null) {
                            log("Erreur finale : " + error.getMessage());
                        } else {
                            log("Indice de confort calculé : " + value);
                        }
                    });

            log("Résultat → %.2f (calcul en %d ms)".formatted(
                    comfortFuture.get(),
                    Duration.between(start, Instant.now()).toMillis()));
        }
    }

    private static double loadTemperature(String city) {
        simulateDelay(250);
        return 17 + ThreadLocalRandom.current().nextDouble(10);
    }

    private static double loadHumidity(String city) {
        simulateDelay(350);
        if (ThreadLocalRandom.current().nextInt(5) == 0) {
            throw new IllegalStateException("Capteur d'humidité hors ligne");
        }
        return 40 + ThreadLocalRandom.current().nextDouble(20);
    }

    private static double computeComfortIndex(double temperature, double humidity) {
        double tempScore = 100 - Math.abs(22 - temperature) * 3;
        double humidityScore = 100 - Math.abs(50 - humidity) * 1.2;
        return Math.max(0, (tempScore + humidityScore) / 2);
    }

    private static void simulateDelay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void log(String message) {
        System.out.printf("[%s][%s] %s%n", Instant.now(), Thread.currentThread().getName(), message);
    }
}
