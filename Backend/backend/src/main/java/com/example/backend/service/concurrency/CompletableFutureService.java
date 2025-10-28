package com.example.backend.service.concurrency;

import com.example.backend.dto.async.AsyncTaskResult;
import com.example.backend.dto.async.ConcurrentTasksResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Service démontrant l'utilisation avancée de CompletableFuture
 * CompletableFuture offre une API fluide pour la programmation asynchrone
 */
@Service
@Slf4j
public class CompletableFutureService {

    private final Executor virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();

    /**
     * Méthode asynchrone simple avec @Async
     * Spring gère automatiquement l'exécution sur un thread séparé
     */
    @Async("ioIntensiveExecutor")
    public CompletableFuture<String> simpleAsyncOperation(String input) {
        try {
            Thread.sleep(500);
            String result = "Traité: " + input;
            log.info("Opération terminée sur thread: {}", Thread.currentThread().getName());
            return CompletableFuture.completedFuture(result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Composition de plusieurs CompletableFutures avec thenCompose
     * Chaîne des opérations asynchrones séquentiellement
     */
    public CompletableFuture<AsyncTaskResult<String>> chainedOperations(String initialData) {
        LocalDateTime startTime = LocalDateTime.now();

        return CompletableFuture.supplyAsync(() -> {
            log.info("Étape 1: Récupération des données");
            sleep(200);
            return "Données: " + initialData;
        }, virtualExecutor)
        .thenCompose(data -> CompletableFuture.supplyAsync(() -> {
            log.info("Étape 2: Validation des données");
            sleep(150);
            return data + " -> Validé";
        }, virtualExecutor))
        .thenCompose(validated -> CompletableFuture.supplyAsync(() -> {
            log.info("Étape 3: Enrichissement des données");
            sleep(100);
            return validated + " -> Enrichi";
        }, virtualExecutor))
        .thenApply(result -> AsyncTaskResult.of(result, startTime, "CompletableFuture chaining"));
    }

    /**
     * Exécution parallèle de plusieurs tâches avec allOf
     * Attend que toutes les tâches se terminent
     */
    public CompletableFuture<ConcurrentTasksResult<String>> executeInParallel(int numberOfTasks) {
        LocalDateTime startTime = LocalDateTime.now();

        List<CompletableFuture<String>> futures = new ArrayList<>();
        for (int i = 0; i < numberOfTasks; i++) {
            int taskId = i;
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                sleep(ThreadLocalRandom.current().nextInt(100, 300));
                return "Résultat de la tâche " + taskId;
            }, virtualExecutor);
            futures.add(future);
        }

        // Combiner tous les futures
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        );

        return allFutures.thenApply(_ -> {
            List<String> results = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

            return ConcurrentTasksResult.of(
                results,
                numberOfTasks,
                results.size(),
                0,
                startTime,
                "CompletableFuture.allOf"
            );
        });
    }

    /**
     * Exécution parallèle avec anyOf
     * Retourne dès que la première tâche se termine
     */
    @SuppressWarnings("unchecked")
    public CompletableFuture<AsyncTaskResult<String>> executeRaceCondition(int numberOfTasks) {
        LocalDateTime startTime = LocalDateTime.now();

        CompletableFuture<String>[] futures = new CompletableFuture[numberOfTasks];
        for (int i = 0; i < numberOfTasks; i++) {
            int taskId = i;
            futures[i] = CompletableFuture.supplyAsync(() -> {
                int delay = ThreadLocalRandom.current().nextInt(50, 500);
                sleep(delay);
                return "Tâche " + taskId + " a gagné (délai: " + delay + "ms)";
            }, virtualExecutor);
        }

        return CompletableFuture.anyOf(futures)
            .thenApply(result -> AsyncTaskResult.of(
                (String) result,
                startTime,
                "CompletableFuture.anyOf"
            ));
    }

    /**
     * Combinaison de deux futures avec thenCombine
     * Exécute les deux en parallèle et combine les résultats
     */
    public CompletableFuture<AsyncTaskResult<String>> combineTwoFutures(String input1, String input2) {
        LocalDateTime startTime = LocalDateTime.now();

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            sleep(200);
            return "Résultat 1: " + input1;
        }, virtualExecutor);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            sleep(150);
            return "Résultat 2: " + input2;
        }, virtualExecutor);

        return future1.thenCombine(future2, (result1, result2) ->
            AsyncTaskResult.of(
                result1 + " + " + result2,
                startTime,
                "CompletableFuture.thenCombine"
            )
        );
    }

    /**
     * Gestion d'erreurs avec exceptionally et handle
     */
    public CompletableFuture<AsyncTaskResult<String>> withErrorHandling(boolean shouldFail) {
        LocalDateTime startTime = LocalDateTime.now();

        return CompletableFuture.supplyAsync(() -> {
            sleep(100);
            if (shouldFail) {
                throw new RuntimeException("Erreur simulée!");
            }
            return "Opération réussie";
        }, virtualExecutor)
        .exceptionally(ex -> {
            log.error("Erreur capturée: {}", ex.getMessage());
            return "Erreur récupérée: " + ex.getMessage();
        })
        .thenApply(result -> AsyncTaskResult.of(result, startTime, "CompletableFuture with error handling"));
    }

    /**
     * Utilisation de handle pour gérer succès et erreur
     */
    public CompletableFuture<AsyncTaskResult<String>> withHandleMethod(boolean shouldFail) {
        LocalDateTime startTime = LocalDateTime.now();

        return CompletableFuture.supplyAsync(() -> {
            sleep(100);
            if (shouldFail) {
                throw new RuntimeException("Erreur dans handle!");
            }
            return "Succès";
        }, virtualExecutor)
        .handle((result, ex) -> {
            if (ex != null) {
                log.error("Gestion de l'erreur: {}", ex.getMessage());
                return "Erreur gérée: " + ex.getMessage();
            }
            return result;
        })
        .thenApply(result -> AsyncTaskResult.of(result, startTime, "CompletableFuture.handle"));
    }

    /**
     * Pattern complexe: Fan-out / Fan-in
     * Distribue le travail puis agrège les résultats
     */
    public CompletableFuture<AsyncTaskResult<String>> fanOutFanIn(List<String> inputs) {
        LocalDateTime startTime = LocalDateTime.now();

        // Fan-out: créer une tâche pour chaque input
        List<CompletableFuture<String>> futures = inputs.stream()
            .map(input -> CompletableFuture.supplyAsync(() -> {
                sleep(ThreadLocalRandom.current().nextInt(50, 200));
                return input.toUpperCase();
            }, virtualExecutor))
            .toList();

        // Fan-in: combiner tous les résultats
        CompletableFuture<Void> allDone = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        );

        return allDone.thenApply(_ -> {
            String combined = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.joining(", "));

            return AsyncTaskResult.of(
                combined,
                startTime,
                "Fan-out/Fan-in pattern"
            );
        });
    }

    /**
     * Timeout sur CompletableFuture (Java 9+)
     */
    public CompletableFuture<AsyncTaskResult<String>> withTimeout(int delayMs, int timeoutMs) {
        LocalDateTime startTime = LocalDateTime.now();

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            sleep(delayMs);
            return "Opération terminée après " + delayMs + "ms";
        }, virtualExecutor);

        return future
            .orTimeout(timeoutMs, java.util.concurrent.TimeUnit.MILLISECONDS)
            .handle((result, ex) -> {
                if (ex != null) {
                    return AsyncTaskResult.of(
                        "Timeout après " + timeoutMs + "ms",
                        startTime,
                        "CompletableFuture with timeout"
                    );
                }
                return AsyncTaskResult.of(result, startTime, "CompletableFuture with timeout");
            });
    }

    /**
     * CompleteOnTimeout - fournit une valeur par défaut en cas de timeout
     */
    public CompletableFuture<AsyncTaskResult<String>> withDefaultOnTimeout(int delayMs, int timeoutMs) {
        LocalDateTime startTime = LocalDateTime.now();

        return CompletableFuture.supplyAsync(() -> {
            sleep(delayMs);
            return "Opération terminée";
        }, virtualExecutor)
        .completeOnTimeout("Valeur par défaut (timeout)", timeoutMs, java.util.concurrent.TimeUnit.MILLISECONDS)
        .thenApply(result -> AsyncTaskResult.of(result, startTime, "CompletableFuture.completeOnTimeout"));
    }

    // Méthode utilitaire
    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
