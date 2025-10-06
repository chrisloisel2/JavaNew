package com.example.java21.workshop;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Outils de debugging et d'optimisation pour les applications concurrentes.
 */
public final class ConcurrentDebuggingToolkit {

    public static void main(String[] args) {
        var toolkit = new ConcurrentDebuggingToolkit();
        toolkit.run();
    }

    private final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    private void run() {
        var start = Instant.now();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var task = CompletableFuture.supplyAsync(() -> heavyComputation("A"), executor);
            var diagnostic = task.thenApply(result -> result + " (optimisé)")
                    .whenComplete((value, error) -> dumpThreadMetrics("calculation", error));

            var fallback = task.orTimeout(750, TimeUnit.MILLISECONDS)
                    .exceptionally(ex -> {
                        dumpThreadMetrics("timeout", ex);
                        return "Valeur par défaut";
                    });

            System.out.println("Résultat = " + diagnostic.thenCombine(fallback, (a, b) -> a + " / " + b).join());
        }

        System.out.printf("Durée totale = %d ms%n", Duration.between(start, Instant.now()).toMillis());
    }

    private String heavyComputation(String prefix) {
        try {
            Thread.sleep(300 + ThreadLocalRandom.current().nextInt(400));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return prefix + "-" + ThreadLocalRandom.current().nextInt(10_000);
    }

    private void dumpThreadMetrics(String stage, Throwable error) {
        var threadCount = threadMXBean.getThreadCount();
        var peak = threadMXBean.getPeakThreadCount();
        var cpuTimeSupported = threadMXBean.isThreadCpuTimeSupported();
        System.out.printf("[%s] threads=%d (pic=%d) CPUTime=%s erreur=%s%n",
                stage,
                threadCount,
                peak,
                cpuTimeSupported ? "activé" : "non supporté",
                error != null ? error.getMessage() : "aucune");
    }
}
