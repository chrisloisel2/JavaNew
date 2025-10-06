package com.example.java21.workshop;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Démonstration des API {@link ProcessHandle} introduites à partir de Java 9.
 */
public final class ProcessHandleExplorer {

    public static void main(String[] args) throws Exception {
        var current = ProcessHandle.current();
        var info = current.info();
        System.out.printf("Processus courant → PID=%d, commande=%s%n", current.pid(), info.command().orElse("<java>"));

        var snapshot = ProcessHandle.allProcesses()
                .limit(5)
                .map(handle -> "%d:%s".formatted(handle.pid(), handle.info().command().orElse("?")))
                .collect(Collectors.joining(", "));
        System.out.println("Processus visibles → " + snapshot);

        var exporter = new ProcessBuilder("bash", "-lc", "echo 'rapport' && sleep 0.5")
                .start();
        var exporterHandle = exporter.toHandle();
        System.out.printf("Sous-processus lancé PID=%d%n", exporterHandle.pid());

        var startedAt = Instant.now();
        exporterHandle.onExit().thenAccept(ph -> {
            var duration = Duration.between(startedAt, Instant.now());
            var cpu = ph.info().totalCpuDuration().map(Duration::toMillis).orElse(0L);
            System.out.printf("Processus %d terminé en %d ms (CPU=%d ms, exit=%d)%n",
                    ph.pid(), duration.toMillis(), cpu, exporter.exitValue());
        }).join();

        var mostRecent = ProcessHandle.allProcesses()
                .filter(handle -> handle.info().startInstant().isPresent())
                .max(Comparator.comparing(handle -> handle.info().startInstant().orElse(Instant.EPOCH)));

        mostRecent.ifPresent(handle -> System.out.printf("Processus le plus récent → PID=%d, démarré à %s%n",
                handle.pid(), handle.info().startInstant().orElse(Instant.EPOCH)));
    }
}
