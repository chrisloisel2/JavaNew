package com.example.backend.dto.async;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Résultat de l'exécution de plusieurs tâches concurrentes
 */
public record ConcurrentTasksResult<T>(
        List<T> results,
        int totalTasks,
        int successfulTasks,
        int failedTasks,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Duration totalExecutionTime,
        String concurrencyStrategy
) {
    public static <T> ConcurrentTasksResult<T> of(
            List<T> results,
            int totalTasks,
            int successfulTasks,
            int failedTasks,
            LocalDateTime startTime,
            String strategy
    ) {
        LocalDateTime endTime = LocalDateTime.now();
        return new ConcurrentTasksResult<>(
                results,
                totalTasks,
                successfulTasks,
                failedTasks,
                startTime,
                endTime,
                Duration.between(startTime, endTime),
                strategy
        );
    }
}
