package com.example.backend.service.concurrency;

import com.example.backend.dto.async.AsyncTaskResult;
import com.example.backend.dto.async.ConcurrentTasksResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Service démontrant l'utilisation de Structured Concurrency (Java 21+)
 *
 * Structured Concurrency garantit que:
 * - Toutes les sous-tâches se terminent avant la tâche principale
 * - Si une tâche échoue, toutes les autres sont annulées
 * - Les ressources sont correctement libérées
 * - Pas de "thread leaks"
 */
@Service
@Slf4j
public class StructuredConcurrencyService {

    /**
     * Exécute plusieurs tâches en parallèle avec ShutdownOnFailure
     * Si une tâche échoue, toutes les autres sont annulées
     */
    public ConcurrentTasksResult<String> executeWithFailureHandling(int numberOfTasks) {
        LocalDateTime startTime = LocalDateTime.now();
        List<String> results = new ArrayList<>();
        int successful = 0;
        int failed = 0;

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // Lancer plusieurs tâches en parallèle
            List<StructuredTaskScope.Subtask<String>> subtasks = new ArrayList<>();

            for (int i = 0; i < numberOfTasks; i++) {
                int taskId = i;
                var subtask = scope.fork(() -> executeTask(taskId));
                subtasks.add(subtask);
            }

            // Attendre que toutes les tâches se terminent ou qu'une échoue
            scope.join();
            scope.throwIfFailed();

            // Collecter les résultats
            for (var subtask : subtasks) {
                String result = subtask.get();
                results.add(result);
                successful++;
            }

        } catch (Exception e) {
            log.error("Une tâche a échoué, annulation de toutes les autres", e);
            failed = numberOfTasks - successful;
        }

        return ConcurrentTasksResult.of(
                results,
                numberOfTasks,
                successful,
                failed,
                startTime,
                "StructuredTaskScope.ShutdownOnFailure"
        );
    }

    /**
     * Exécute plusieurs tâches en parallèle avec ShutdownOnSuccess
     * S'arrête dès que la première tâche réussit
     */
    public AsyncTaskResult<String> executeUntilFirstSuccess(int numberOfTasks) {
        LocalDateTime startTime = LocalDateTime.now();
        String result = null;

        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
            // Lancer plusieurs tâches en parallèle
            for (int i = 0; i < numberOfTasks; i++) {
                int taskId = i;
                scope.fork(() -> searchForResult(taskId));
            }

            // Attendre la première réussite
            scope.join();
            result = scope.result();

            log.info("Première tâche réussie trouvée: {}", result);

        } catch (Exception e) {
            log.error("Aucune tâche n'a réussi", e);
            result = "Erreur: " + e.getMessage();
        }

        return AsyncTaskResult.of(result, startTime, "StructuredTaskScope.ShutdownOnSuccess");
    }

    /**
     * Exemple avec traitement hiérarchique de tâches
     * Démontre la composition de scopes
     */
    public ConcurrentTasksResult<String> executeHierarchicalTasks() {
        LocalDateTime startTime = LocalDateTime.now();
        List<String> allResults = new ArrayList<>();
        int successful = 0;

        try (var parentScope = new StructuredTaskScope.ShutdownOnFailure()) {
            // Tâche 1: Récupérer des données utilisateur
            var userTask = parentScope.fork(() -> {
                Thread.sleep(100);
                return "Utilisateurs récupérés";
            });

            // Tâche 2: Récupérer des données de livres
            var booksTask = parentScope.fork(() -> {
                Thread.sleep(150);
                return "Livres récupérés";
            });

            // Tâche 3: Récupérer des statistiques (dépend des autres)
            var statsTask = parentScope.fork(() -> {
                Thread.sleep(80);
                return "Statistiques calculées";
            });

            parentScope.join();
            parentScope.throwIfFailed();

            allResults.add(userTask.get());
            allResults.add(booksTask.get());
            allResults.add(statsTask.get());
            successful = 3;

        } catch (Exception e) {
            log.error("Erreur dans le traitement hiérarchique", e);
        }

        return ConcurrentTasksResult.of(
                allResults,
                3,
                successful,
                3 - successful,
                startTime,
                "Hierarchical StructuredTaskScope"
        );
    }

    /**
     * Comparaison avec une approche non-structurée (à éviter)
     * Démontre les problèmes potentiels sans structured concurrency
     */
    public ConcurrentTasksResult<String> executeUnstructured(int numberOfTasks) {
        LocalDateTime startTime = LocalDateTime.now();
        List<String> results = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        // ⚠️ Approche déconseillée - juste pour démonstration
        for (int i = 0; i < numberOfTasks; i++) {
            int taskId = i;
            Thread thread = Thread.startVirtualThread(() -> {
                try {
                    String result = executeTask(taskId);
                    synchronized (results) {
                        results.add(result);
                    }
                } catch (Exception e) {
                    log.error("Erreur dans la tâche {}", taskId, e);
                }
            });
            threads.add(thread);
        }

        // Attendre manuellement tous les threads
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return ConcurrentTasksResult.of(
                results,
                numberOfTasks,
                results.size(),
                numberOfTasks - results.size(),
                startTime,
                "Unstructured (manual thread management)"
        );
    }

    // Méthodes utilitaires privées

    private String executeTask(int taskId) throws InterruptedException {
        Thread.sleep(ThreadLocalRandom.current().nextInt(100, 500));
        log.debug("Tâche {} terminée sur thread {}", taskId, Thread.currentThread().getName());
        return "Résultat de la tâche " + taskId;
    }

    private String searchForResult(int taskId) throws InterruptedException {
        // Simule une recherche qui peut prendre du temps
        int delay = ThreadLocalRandom.current().nextInt(50, 300);
        Thread.sleep(delay);

        // Certaines tâches trouvent le résultat plus rapidement
        if (taskId % 3 == 0 && delay < 150) {
            return "Résultat trouvé par la tâche " + taskId;
        }

        throw new RuntimeException("Résultat non trouvé par la tâche " + taskId);
    }
}
