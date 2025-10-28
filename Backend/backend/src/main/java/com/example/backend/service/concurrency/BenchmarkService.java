package com.example.backend.service.concurrency;

import com.example.backend.dto.async.PerformanceMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.StructuredTaskScope;
import java.util.stream.IntStream;

/**
 * Service de benchmark comparant toutes les approches de multithreading
 *
 * Compare:
 * - Structured Concurrency (Java 21+)
 * - CompletableFuture avec Virtual Threads
 * - CompletableFuture traditionnel
 * - Project Reactor
 * - Parallel Streams
 * - Séquentiel (baseline)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BenchmarkService {

    /**
     * Benchmark complet de toutes les approches
     */
    public Map<String, PerformanceMetrics> runComprehensiveBenchmark(int numberOfTasks) {
        Map<String, PerformanceMetrics> results = new HashMap<>();

        log.info("Starting comprehensive benchmark with {} tasks", numberOfTasks);

        // 1. Baseline: Séquentiel
        results.put("sequential", benchmarkSequential(numberOfTasks));

        // 2. Structured Concurrency
        results.put("structured_concurrency", benchmarkStructuredConcurrency(numberOfTasks));

        // 3. CompletableFuture avec Virtual Threads
        results.put("completable_future_virtual", benchmarkCompletableFutureVirtual(numberOfTasks));

        // 4. CompletableFuture traditionnel
        results.put("completable_future_traditional", benchmarkCompletableFutureTraditional(numberOfTasks));

        // 5. Project Reactor
        results.put("reactor", benchmarkReactor(numberOfTasks));

        // 6. Parallel Streams
        results.put("parallel_streams", benchmarkParallelStreams(numberOfTasks));

        log.info("Benchmark completed");
        return results;
    }

    /**
     * Benchmark I/O intensif (simule des appels réseau, DB, etc.)
     */
    public Map<String, PerformanceMetrics> benchmarkIOIntensive(int numberOfTasks) {
        Map<String, PerformanceMetrics> results = new HashMap<>();

        log.info("Starting I/O intensive benchmark with {} tasks", numberOfTasks);

        results.put("virtual_threads", benchmarkIOWithVirtualThreads(numberOfTasks));
        results.put("reactor", benchmarkIOWithReactor(numberOfTasks));
        results.put("completable_future", benchmarkIOWithCompletableFuture(numberOfTasks));

        return results;
    }

    /**
     * Benchmark CPU intensif
     */
    public Map<String, PerformanceMetrics> benchmarkCPUIntensive(int numberOfTasks) {
        Map<String, PerformanceMetrics> results = new HashMap<>();

        log.info("Starting CPU intensive benchmark with {} tasks", numberOfTasks);

        results.put("sequential", benchmarkCPUSequential(numberOfTasks));
        results.put("parallel_streams", benchmarkCPUParallelStreams(numberOfTasks));
        results.put("reactor_parallel", benchmarkCPUReactorParallel(numberOfTasks));

        return results;
    }

    // ============ IMPLÉMENTATIONS DES BENCHMARKS ============

    private PerformanceMetrics benchmarkSequential(int numberOfTasks) {
        LocalDateTime start = LocalDateTime.now();

        for (int i = 0; i < numberOfTasks; i++) {
            simulateTask(100);
        }

        Duration duration = Duration.between(start, LocalDateTime.now());
        return PerformanceMetrics.create("Sequential", duration, numberOfTasks, 1, false);
    }

    private PerformanceMetrics benchmarkStructuredConcurrency(int numberOfTasks) {
        LocalDateTime start = LocalDateTime.now();

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            for (int i = 0; i < numberOfTasks; i++) {
                scope.fork(() -> {
                    simulateTask(100);
                    return "done";
                });
            }
            scope.join();
        } catch (Exception e) {
            log.error("Structured concurrency benchmark failed", e);
        }

        Duration duration = Duration.between(start, LocalDateTime.now());
        return PerformanceMetrics.create(
            "Structured Concurrency",
            duration,
            numberOfTasks,
            Runtime.getRuntime().availableProcessors(),
            true
        );
    }

    private PerformanceMetrics benchmarkCompletableFutureVirtual(int numberOfTasks) {
        LocalDateTime start = LocalDateTime.now();
        var executor = Executors.newVirtualThreadPerTaskExecutor();

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < numberOfTasks; i++) {
            futures.add(CompletableFuture.runAsync(() -> simulateTask(100), executor));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executor.close();

        Duration duration = Duration.between(start, LocalDateTime.now());
        return PerformanceMetrics.create(
            "CompletableFuture + Virtual Threads",
            duration,
            numberOfTasks,
            numberOfTasks, // Virtual threads
            true
        );
    }

    private PerformanceMetrics benchmarkCompletableFutureTraditional(int numberOfTasks) {
        LocalDateTime start = LocalDateTime.now();
        var executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < numberOfTasks; i++) {
            futures.add(CompletableFuture.runAsync(() -> simulateTask(100), executor));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executor.shutdown();

        Duration duration = Duration.between(start, LocalDateTime.now());
        return PerformanceMetrics.create(
            "CompletableFuture + Traditional Pool",
            duration,
            numberOfTasks,
            Runtime.getRuntime().availableProcessors(),
            false
        );
    }

    private PerformanceMetrics benchmarkReactor(int numberOfTasks) {
        LocalDateTime start = LocalDateTime.now();

        Flux.range(0, numberOfTasks)
            .flatMap(i -> Mono.fromCallable(() -> {
                simulateTask(100);
                return i;
            }).subscribeOn(Schedulers.boundedElastic()))
            .blockLast();

        Duration duration = Duration.between(start, LocalDateTime.now());
        return PerformanceMetrics.create(
            "Project Reactor",
            duration,
            numberOfTasks,
            Schedulers.DEFAULT_BOUNDED_ELASTIC_SIZE,
            false
        );
    }

    private PerformanceMetrics benchmarkParallelStreams(int numberOfTasks) {
        LocalDateTime start = LocalDateTime.now();

        IntStream.range(0, numberOfTasks)
            .parallel()
            .forEach(i -> simulateTask(100));

        Duration duration = Duration.between(start, LocalDateTime.now());
        return PerformanceMetrics.create(
            "Parallel Streams",
            duration,
            numberOfTasks,
            Runtime.getRuntime().availableProcessors(),
            false
        );
    }

    // ============ BENCHMARKS I/O INTENSIFS ============

    private PerformanceMetrics benchmarkIOWithVirtualThreads(int numberOfTasks) {
        LocalDateTime start = LocalDateTime.now();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (int i = 0; i < numberOfTasks; i++) {
                futures.add(CompletableFuture.runAsync(() -> simulateIOTask(50), executor));
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }

        Duration duration = Duration.between(start, LocalDateTime.now());
        return PerformanceMetrics.create(
            "Virtual Threads (I/O)",
            duration,
            numberOfTasks,
            numberOfTasks,
            true
        );
    }

    private PerformanceMetrics benchmarkIOWithReactor(int numberOfTasks) {
        LocalDateTime start = LocalDateTime.now();

        Flux.range(0, numberOfTasks)
            .flatMap(i -> Mono.fromCallable(() -> {
                simulateIOTask(50);
                return i;
            }).subscribeOn(Schedulers.boundedElastic()))
            .blockLast();

        Duration duration = Duration.between(start, LocalDateTime.now());
        return PerformanceMetrics.create(
            "Reactor (I/O)",
            duration,
            numberOfTasks,
            Schedulers.DEFAULT_BOUNDED_ELASTIC_SIZE,
            false
        );
    }

    private PerformanceMetrics benchmarkIOWithCompletableFuture(int numberOfTasks) {
        LocalDateTime start = LocalDateTime.now();
        var executor = Executors.newFixedThreadPool(100); // Pool large pour I/O

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < numberOfTasks; i++) {
            futures.add(CompletableFuture.runAsync(() -> simulateIOTask(50), executor));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executor.shutdown();

        Duration duration = Duration.between(start, LocalDateTime.now());
        return PerformanceMetrics.create(
            "CompletableFuture (I/O)",
            duration,
            numberOfTasks,
            100,
            false
        );
    }

    // ============ BENCHMARKS CPU INTENSIFS ============

    private PerformanceMetrics benchmarkCPUSequential(int numberOfTasks) {
        LocalDateTime start = LocalDateTime.now();

        for (int i = 0; i < numberOfTasks; i++) {
            cpuIntensiveTask();
        }

        Duration duration = Duration.between(start, LocalDateTime.now());
        return PerformanceMetrics.create(
            "Sequential (CPU)",
            duration,
            numberOfTasks,
            1,
            false
        );
    }

    private PerformanceMetrics benchmarkCPUParallelStreams(int numberOfTasks) {
        LocalDateTime start = LocalDateTime.now();

        IntStream.range(0, numberOfTasks)
            .parallel()
            .forEach(i -> cpuIntensiveTask());

        Duration duration = Duration.between(start, LocalDateTime.now());
        return PerformanceMetrics.create(
            "Parallel Streams (CPU)",
            duration,
            numberOfTasks,
            Runtime.getRuntime().availableProcessors(),
            false
        );
    }

    private PerformanceMetrics benchmarkCPUReactorParallel(int numberOfTasks) {
        LocalDateTime start = LocalDateTime.now();

        Flux.range(0, numberOfTasks)
            .parallel()
            .runOn(Schedulers.parallel())
            .map(i -> {
                cpuIntensiveTask();
                return i;
            })
            .sequential()
            .blockLast();

        Duration duration = Duration.between(start, LocalDateTime.now());
        return PerformanceMetrics.create(
            "Reactor Parallel (CPU)",
            duration,
            numberOfTasks,
            Runtime.getRuntime().availableProcessors(),
            false
        );
    }

    /**
     * Benchmark de scalabilité - teste avec différents nombres de tâches
     */
    public Map<Integer, Map<String, PerformanceMetrics>> scalabilityBenchmark() {
        Map<Integer, Map<String, PerformanceMetrics>> results = new HashMap<>();

        int[] taskCounts = {10, 50, 100, 500, 1000};

        for (int taskCount : taskCounts) {
            log.info("Running scalability benchmark with {} tasks", taskCount);
            results.put(taskCount, Map.of(
                "virtual_threads", benchmarkIOWithVirtualThreads(taskCount),
                "reactor", benchmarkIOWithReactor(taskCount),
                "parallel_streams", benchmarkParallelStreams(taskCount)
            ));
        }

        return results;
    }

    // ============ MÉTHODES UTILITAIRES ============

    /**
     * Simule une tâche générique
     */
    private void simulateTask(int delayMs) {
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    /**
     * Simule une opération I/O (réseau, DB, fichier...)
     */
    private void simulateIOTask(int delayMs) {
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    /**
     * Simule une opération CPU intensive
     */
    private long cpuIntensiveTask() {
        long result = 0;
        for (int i = 0; i < 100000; i++) {
            result += Math.sqrt(i) * Math.sin(i);
        }
        return result;
    }
}
