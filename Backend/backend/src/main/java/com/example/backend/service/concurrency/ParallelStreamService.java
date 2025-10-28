package com.example.backend.service.concurrency;

import com.example.backend.dto.async.ConcurrentTasksResult;
import com.example.backend.dto.async.PerformanceMetrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Service démontrant l'utilisation des Parallel Streams (Java 8+)
 *
 * Concepts clés:
 * - Parallel Streams: traitement parallèle automatique avec le ForkJoinPool commun
 * - Performance: bon pour CPU-bound, moins pour I/O-bound
 * - Thread-safety: nécessite des opérations thread-safe
 */
@Service
@Slf4j
public class ParallelStreamService {

    /**
     * Comparaison entre Stream séquentiel et parallèle
     */
    public Map<String, PerformanceMetrics> compareSequentialVsParallel(int numberOfElements) {
        Map<String, PerformanceMetrics> results = new ConcurrentHashMap<>();

        // Test séquentiel
        LocalDateTime seqStart = LocalDateTime.now();
        List<Integer> seqResults = IntStream.range(0, numberOfElements)
            .map(this::cpuIntensiveOperation)
            .boxed()
            .toList();
        Duration seqDuration = Duration.between(seqStart, LocalDateTime.now());

        results.put("sequential", PerformanceMetrics.create(
            "Sequential Stream",
            seqDuration,
            seqResults.size(),
            1,
            false
        ));

        // Test parallèle
        LocalDateTime parStart = LocalDateTime.now();
        List<Integer> parResults = IntStream.range(0, numberOfElements)
            .parallel()
            .map(this::cpuIntensiveOperation)
            .boxed()
            .toList();
        Duration parDuration = Duration.between(parStart, LocalDateTime.now());

        int parallelism = Runtime.getRuntime().availableProcessors();
        results.put("parallel", PerformanceMetrics.create(
            "Parallel Stream",
            parDuration,
            parResults.size(),
            parallelism,
            false
        ));

        log.info("Sequential: {}ms, Parallel: {}ms, Speedup: {}x",
            seqDuration.toMillis(),
            parDuration.toMillis(),
            (double) seqDuration.toMillis() / parDuration.toMillis()
        );

        return results;
    }

    /**
     * Traitement parallèle simple avec collect
     */
    public ConcurrentTasksResult<String> simpleParallelProcessing(int numberOfElements) {
        LocalDateTime startTime = LocalDateTime.now();

        List<String> results = IntStream.range(0, numberOfElements)
            .parallel()
            .mapToObj(i -> {
                int value = cpuIntensiveOperation(i);
                return "Résultat " + i + ": " + value;
            })
            .toList();

        return ConcurrentTasksResult.of(
            results,
            numberOfElements,
            results.size(),
            0,
            startTime,
            "Parallel Stream"
        );
    }

    /**
     * Opérations de réduction parallèle (sum, average, etc.)
     */
    public Map<String, Object> parallelReductions(int numberOfElements) {
        LocalDateTime startTime = LocalDateTime.now();

        // Sum
        long sum = IntStream.range(0, numberOfElements)
            .parallel()
            .asLongStream()
            .sum();

        // Average
        double average = IntStream.range(0, numberOfElements)
            .parallel()
            .average()
            .orElse(0.0);

        // Min/Max
        int min = IntStream.range(0, numberOfElements)
            .parallel()
            .min()
            .orElse(0);

        int max = IntStream.range(0, numberOfElements)
            .parallel()
            .max()
            .orElse(0);

        // Count
        long count = IntStream.range(0, numberOfElements)
            .parallel()
            .count();

        Duration duration = Duration.between(startTime, LocalDateTime.now());

        return Map.of(
            "sum", sum,
            "average", average,
            "min", min,
            "max", max,
            "count", count,
            "executionTimeMs", duration.toMillis()
        );
    }

    /**
     * Grouping et partitioning en parallèle
     */
    public Map<String, Object> parallelGroupingAndPartitioning(int numberOfElements) {
        List<Integer> numbers = IntStream.range(0, numberOfElements)
            .boxed()
            .toList();

        LocalDateTime startTime = LocalDateTime.now();

        // Grouping by (modulo 10)
        Map<Integer, List<Integer>> grouped = numbers.parallelStream()
            .collect(Collectors.groupingByConcurrent(n -> n % 10));

        // Partitioning (even/odd)
        Map<Boolean, List<Integer>> partitioned = numbers.parallelStream()
            .collect(Collectors.partitioningBy(n -> n % 2 == 0));

        Duration duration = Duration.between(startTime, LocalDateTime.now());

        return Map.of(
            "groupedSize", grouped.size(),
            "evenCount", partitioned.get(true).size(),
            "oddCount", partitioned.get(false).size(),
            "executionTimeMs", duration.toMillis()
        );
    }

    /**
     * Démonstration de l'importance de la thread-safety
     */
    public Map<String, Object> threadSafetyDemo(int numberOfElements) {
        // ⚠️ NON thread-safe (pour démonstration)
        AtomicInteger unsafeCounter = new AtomicInteger(0);
        List<Integer> unsafeList = new java.util.ArrayList<>();

        // ❌ Ceci peut causer des problèmes
        IntStream.range(0, numberOfElements)
            .parallel()
            .forEach(i -> {
                unsafeCounter.incrementAndGet();
                // unsafeList.add(i); // ⚠️ ConcurrentModificationException possible
            });

        // ✅ Thread-safe avec Collectors
        List<Integer> safeList = IntStream.range(0, numberOfElements)
            .parallel()
            .boxed()
            .collect(Collectors.toList());

        // ✅ Thread-safe avec synchronized collection
        List<Integer> syncList = java.util.Collections.synchronizedList(new java.util.ArrayList<>());
        IntStream.range(0, numberOfElements)
            .parallel()
            .forEach(syncList::add);

        // ✅ Thread-safe avec ConcurrentHashMap
        Map<Integer, String> concurrentMap = new ConcurrentHashMap<>();
        IntStream.range(0, numberOfElements)
            .parallel()
            .forEach(i -> concurrentMap.put(i, "Value " + i));

        return Map.of(
            "unsafeCounterValue", unsafeCounter.get(),
            "safeListSize", safeList.size(),
            "syncListSize", syncList.size(),
            "concurrentMapSize", concurrentMap.size()
        );
    }

    /**
     * Quand NE PAS utiliser parallel streams
     * Démontre les cas où parallel est contre-productif
     */
    public Map<String, PerformanceMetrics> whenNotToUseParallel(int numberOfElements) {
        Map<String, PerformanceMetrics> results = new ConcurrentHashMap<>();

        // Cas 1: Petite collection
        List<Integer> smallList = IntStream.range(0, 100).boxed().toList();

        LocalDateTime seqSmallStart = LocalDateTime.now();
        smallList.stream().map(i -> i * 2).toList();
        Duration seqSmallDuration = Duration.between(seqSmallStart, LocalDateTime.now());

        LocalDateTime parSmallStart = LocalDateTime.now();
        smallList.parallelStream().map(i -> i * 2).toList();
        Duration parSmallDuration = Duration.between(parSmallStart, LocalDateTime.now());

        results.put("small_sequential", PerformanceMetrics.create(
            "Small Sequential",
            seqSmallDuration,
            100,
            1,
            false
        ));

        results.put("small_parallel", PerformanceMetrics.create(
            "Small Parallel (overhead!)",
            parSmallDuration,
            100,
            Runtime.getRuntime().availableProcessors(),
            false
        ));

        // Cas 2: Opération I/O (simulée)
        LocalDateTime seqIoStart = LocalDateTime.now();
        IntStream.range(0, numberOfElements)
            .mapToObj(this::ioOperation)
            .toList();
        Duration seqIoDuration = Duration.between(seqIoStart, LocalDateTime.now());

        LocalDateTime parIoStart = LocalDateTime.now();
        IntStream.range(0, numberOfElements)
            .parallel()
            .mapToObj(this::ioOperation)
            .toList();
        Duration parIoDuration = Duration.between(parIoStart, LocalDateTime.now());

        results.put("io_sequential", PerformanceMetrics.create(
            "I/O Sequential",
            seqIoDuration,
            numberOfElements,
            1,
            false
        ));

        results.put("io_parallel", PerformanceMetrics.create(
            "I/O Parallel (ForkJoinPool not ideal)",
            parIoDuration,
            numberOfElements,
            Runtime.getRuntime().availableProcessors(),
            false
        ));

        return results;
    }

    /**
     * Utilisation de findAny et findFirst
     */
    public Map<String, Object> findOperations(int numberOfElements) {
        List<Integer> numbers = IntStream.range(0, numberOfElements)
            .boxed()
            .toList();

        // findFirst: garantit le premier élément même en parallèle
        LocalDateTime firstStart = LocalDateTime.now();
        Integer first = numbers.parallelStream()
            .filter(n -> n > numberOfElements / 2)
            .findFirst()
            .orElse(-1);
        Duration firstDuration = Duration.between(firstStart, LocalDateTime.now());

        // findAny: peut retourner n'importe quel élément (plus rapide en parallèle)
        LocalDateTime anyStart = LocalDateTime.now();
        Integer any = numbers.parallelStream()
            .filter(n -> n > numberOfElements / 2)
            .findAny()
            .orElse(-1);
        Duration anyDuration = Duration.between(anyStart, LocalDateTime.now());

        return Map.of(
            "findFirstResult", first,
            "findFirstTimeMs", firstDuration.toMillis(),
            "findAnyResult", any,
            "findAnyTimeMs", anyDuration.toMillis(),
            "note", "findAny est généralement plus rapide en parallèle"
        );
    }

    /**
     * Short-circuit operations (anyMatch, allMatch, noneMatch)
     */
    public Map<String, Object> shortCircuitOperations(int numberOfElements) {
        List<Integer> numbers = IntStream.range(0, numberOfElements)
            .boxed()
            .toList();

        LocalDateTime startTime = LocalDateTime.now();

        boolean anyMatch = numbers.parallelStream()
            .anyMatch(n -> n > numberOfElements / 2);

        boolean allMatch = numbers.parallelStream()
            .allMatch(n -> n >= 0);

        boolean noneMatch = numbers.parallelStream()
            .noneMatch(n -> n < 0);

        Duration duration = Duration.between(startTime, LocalDateTime.now());

        return Map.of(
            "anyMatch", anyMatch,
            "allMatch", allMatch,
            "noneMatch", noneMatch,
            "executionTimeMs", duration.toMillis(),
            "benefit", "Ces opérations peuvent s'arrêter dès qu'une condition est remplie"
        );
    }

    /**
     * Démonstration de l'ordre d'exécution
     */
    public Map<String, List<String>> executionOrderDemo(int numberOfElements) {
        // Sequential: ordre préservé
        List<String> sequential = IntStream.range(0, numberOfElements)
            .mapToObj(i -> "Seq-" + i)
            .toList();

        // Parallel: ordre non garanti pendant l'exécution mais préservé dans le résultat final
        List<String> parallel = IntStream.range(0, numberOfElements)
            .parallel()
            .mapToObj(i -> {
                log.debug("Processing {} on thread {}", i, Thread.currentThread().getName());
                return "Par-" + i;
            })
            .toList();

        // forEachOrdered: ordre garanti même en parallèle
        List<String> ordered = IntStream.range(0, numberOfElements)
            .parallel()
            .mapToObj(i -> "Ordered-" + i)
            .toList(); // collect préserve l'ordre

        return Map.of(
            "sequential", sequential,
            "parallel", parallel,
            "ordered", ordered
        );
    }

    // Méthodes utilitaires

    /**
     * Simule une opération CPU intensive
     */
    private int cpuIntensiveOperation(int input) {
        // Calcul factoriel ou fibonacci
        int result = 1;
        for (int i = 1; i <= 20; i++) {
            result *= i;
            result = result % 1000000; // Évite l'overflow
        }
        return result + input;
    }

    /**
     * Simule une opération I/O
     */
    private String ioOperation(int input) {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(10, 50));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "I/O result: " + input;
    }
}
