package com.example.backend.dto.async;

import java.time.Duration;

/**
 * Métriques de performance pour comparer différentes approches de multithreading
 */
public record PerformanceMetrics(
        String approach,
        Duration executionTime,
        int tasksCompleted,
        double tasksPerSecond,
        long memoryUsedMB,
        int threadsUsed,
        boolean usedVirtualThreads
) {
    public static PerformanceMetrics create(
            String approach,
            Duration executionTime,
            int tasksCompleted,
            int threadsUsed,
            boolean usedVirtualThreads
    ) {
        double tasksPerSecond = tasksCompleted / Math.max(executionTime.toMillis() / 1000.0, 0.001);
        Runtime runtime = Runtime.getRuntime();
        long memoryUsed = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);

        return new PerformanceMetrics(
                approach,
                executionTime,
                tasksCompleted,
                tasksPerSecond,
                memoryUsed,
                threadsUsed,
                usedVirtualThreads
        );
    }
}
