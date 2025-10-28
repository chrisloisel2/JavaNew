package com.example.backend.dto.async;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Résultat d'une opération asynchrone avec métadonnées
 */
public record AsyncTaskResult<T>(
        T data,
        String threadName,
        boolean isVirtualThread,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Duration executionTime,
        String executorType
) {
    public static <T> AsyncTaskResult<T> of(T data, LocalDateTime startTime, String executorType) {
        LocalDateTime endTime = LocalDateTime.now();
        Thread currentThread = Thread.currentThread();

        return new AsyncTaskResult<>(
                data,
                currentThread.getName(),
                currentThread.isVirtual(),
                startTime,
                endTime,
                Duration.between(startTime, endTime),
                executorType
        );
    }
}
