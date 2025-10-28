package com.example.backend.controller;

import com.example.backend.dto.async.AsyncTaskResult;
import com.example.backend.dto.async.ConcurrentTasksResult;
import com.example.backend.dto.async.PerformanceMetrics;
import com.example.backend.service.concurrency.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Controller démontrant toutes les approches modernes de multithreading en Java
 *
 * Endpoints organisés par technologie:
 * - /api/concurrency/structured-concurrency/* : StructuredTaskScope (Java 21+)
 * - /api/concurrency/completable-future/* : CompletableFuture
 * - /api/concurrency/reactive/* : Project Reactor
 * - /api/concurrency/parallel-streams/* : Parallel Streams
 * - /api/concurrency/benchmark/* : Comparaison de performances
 */
@RestController
@RequestMapping("/api/concurrency")
@RequiredArgsConstructor
@Slf4j
public class ConcurrencyDemoController {

    private final StructuredConcurrencyService structuredConcurrencyService;
    private final CompletableFutureService completableFutureService;
    private final ReactiveService reactiveService;
    private final ParallelStreamService parallelStreamService;
    private final BenchmarkService benchmarkService;

    // ============ STRUCTURED CONCURRENCY (Java 21+) ============

    /**
     * Exécute des tâches avec ShutdownOnFailure
     * GET /api/concurrency/structured-concurrency/shutdown-on-failure?tasks=5
     */
    @GetMapping("/structured-concurrency/shutdown-on-failure")
    public ConcurrentTasksResult<String> structuredWithFailureHandling(
            @RequestParam(defaultValue = "5") int tasks
    ) {
        log.info("Executing {} tasks with StructuredTaskScope.ShutdownOnFailure", tasks);
        return structuredConcurrencyService.executeWithFailureHandling(tasks);
    }

    /**
     * Exécute jusqu'à la première réussite
     * GET /api/concurrency/structured-concurrency/shutdown-on-success?tasks=10
     */
    @GetMapping("/structured-concurrency/shutdown-on-success")
    public AsyncTaskResult<String> structuredUntilFirstSuccess(
            @RequestParam(defaultValue = "10") int tasks
    ) {
        log.info("Executing {} tasks with ShutdownOnSuccess", tasks);
        return structuredConcurrencyService.executeUntilFirstSuccess(tasks);
    }

    /**
     * Tâches hiérarchiques
     * GET /api/concurrency/structured-concurrency/hierarchical
     */
    @GetMapping("/structured-concurrency/hierarchical")
    public ConcurrentTasksResult<String> structuredHierarchical() {
        log.info("Executing hierarchical tasks");
        return structuredConcurrencyService.executeHierarchicalTasks();
    }

    /**
     * Comparaison avec approche non-structurée
     * GET /api/concurrency/structured-concurrency/compare?tasks=10
     */
    @GetMapping("/structured-concurrency/compare")
    public Map<String, ConcurrentTasksResult<String>> compareStructured(
            @RequestParam(defaultValue = "10") int tasks
    ) {
        return Map.of(
            "structured", structuredConcurrencyService.executeWithFailureHandling(tasks),
            "unstructured", structuredConcurrencyService.executeUnstructured(tasks)
        );
    }

    // ============ COMPLETABLE FUTURE ============

    /**
     * Opération asynchrone simple avec @Async
     * GET /api/concurrency/completable-future/simple?input=test
     */
    @GetMapping("/completable-future/simple")
    public CompletableFuture<String> completableFutureSimple(
            @RequestParam(defaultValue = "test") String input
    ) {
        log.info("Simple async operation with input: {}", input);
        return completableFutureService.simpleAsyncOperation(input);
    }

    /**
     * Opérations chaînées (thenCompose)
     * GET /api/concurrency/completable-future/chained?data=myData
     */
    @GetMapping("/completable-future/chained")
    public CompletableFuture<AsyncTaskResult<String>> completableFutureChained(
            @RequestParam(defaultValue = "myData") String data
    ) {
        log.info("Chained operations with data: {}", data);
        return completableFutureService.chainedOperations(data);
    }

    /**
     * Exécution parallèle avec allOf
     * GET /api/concurrency/completable-future/parallel?tasks=10
     */
    @GetMapping("/completable-future/parallel")
    public CompletableFuture<ConcurrentTasksResult<String>> completableFutureParallel(
            @RequestParam(defaultValue = "10") int tasks
    ) {
        log.info("Parallel execution of {} tasks", tasks);
        return completableFutureService.executeInParallel(tasks);
    }

    /**
     * Race condition avec anyOf
     * GET /api/concurrency/completable-future/race?tasks=10
     */
    @GetMapping("/completable-future/race")
    public CompletableFuture<AsyncTaskResult<String>> completableFutureRace(
            @RequestParam(defaultValue = "10") int tasks
    ) {
        log.info("Race condition with {} tasks", tasks);
        return completableFutureService.executeRaceCondition(tasks);
    }

    /**
     * Combine deux futures
     * GET /api/concurrency/completable-future/combine?input1=A&input2=B
     */
    @GetMapping("/completable-future/combine")
    public CompletableFuture<AsyncTaskResult<String>> completableFutureCombine(
            @RequestParam(defaultValue = "A") String input1,
            @RequestParam(defaultValue = "B") String input2
    ) {
        log.info("Combining two futures");
        return completableFutureService.combineTwoFutures(input1, input2);
    }

    /**
     * Gestion d'erreurs
     * GET /api/concurrency/completable-future/error-handling?shouldFail=true
     */
    @GetMapping("/completable-future/error-handling")
    public CompletableFuture<AsyncTaskResult<String>> completableFutureErrorHandling(
            @RequestParam(defaultValue = "false") boolean shouldFail
    ) {
        log.info("Error handling demo, shouldFail: {}", shouldFail);
        return completableFutureService.withErrorHandling(shouldFail);
    }

    /**
     * Fan-out / Fan-in pattern
     * POST /api/concurrency/completable-future/fan-out-fan-in
     */
    @PostMapping("/completable-future/fan-out-fan-in")
    public CompletableFuture<AsyncTaskResult<String>> completableFutureFanOut(
            @RequestBody List<String> inputs
    ) {
        log.info("Fan-out/Fan-in with {} inputs", inputs.size());
        return completableFutureService.fanOutFanIn(inputs);
    }

    /**
     * Timeout demo
     * GET /api/concurrency/completable-future/timeout?delay=100&timeout=200
     */
    @GetMapping("/completable-future/timeout")
    public CompletableFuture<AsyncTaskResult<String>> completableFutureTimeout(
            @RequestParam(defaultValue = "100") int delay,
            @RequestParam(defaultValue = "200") int timeout
    ) {
        log.info("Timeout demo: delay={}ms, timeout={}ms", delay, timeout);
        return completableFutureService.withTimeout(delay, timeout);
    }

    // ============ REACTIVE (PROJECT REACTOR) ============

    /**
     * Mono simple
     * GET /api/concurrency/reactive/mono?input=test
     */
    @GetMapping("/reactive/mono")
    public Mono<AsyncTaskResult<String>> reactiveMono(
            @RequestParam(defaultValue = "test") String input
    ) {
        log.info("Simple Mono operation with input: {}", input);
        return reactiveService.simpleMonoOperation(input);
    }

    /**
     * Flux simple (Server-Sent Events)
     * GET /api/concurrency/reactive/flux?count=10
     */
    @GetMapping(value = "/reactive/flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> reactiveFlux(
            @RequestParam(defaultValue = "10") int count
    ) {
        log.info("Simple Flux operation with {} elements", count);
        return reactiveService.simpleFluxOperation(count);
    }

    /**
     * Traitement parallèle avec Flux
     * GET /api/concurrency/reactive/parallel?elements=20
     */
    @GetMapping("/reactive/parallel")
    public Mono<List<String>> reactiveParallel(
            @RequestParam(defaultValue = "20") int elements
    ) {
        log.info("Parallel Flux processing with {} elements", elements);
        return reactiveService.parallelFluxProcessing(elements);
    }

    /**
     * FlatMap demo
     * POST /api/concurrency/reactive/flatmap
     */
    @PostMapping(value = "/reactive/flatmap", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> reactiveFlatMap(@RequestBody List<String> inputs) {
        log.info("FlatMap with {} inputs", inputs.size());
        return reactiveService.flatMapOperation(inputs);
    }

    /**
     * Zip multiple Monos
     * GET /api/concurrency/reactive/zip?input1=A&input2=B&input3=C
     */
    @GetMapping("/reactive/zip")
    public Mono<AsyncTaskResult<String>> reactiveZip(
            @RequestParam(defaultValue = "A") String input1,
            @RequestParam(defaultValue = "B") String input2,
            @RequestParam(defaultValue = "C") String input3
    ) {
        log.info("Zipping three Monos");
        return reactiveService.zipMultipleMonos(input1, input2, input3);
    }

    /**
     * Merge multiple Flux
     * GET /api/concurrency/reactive/merge
     */
    @GetMapping(value = "/reactive/merge", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> reactiveMerge() {
        log.info("Merging multiple Flux");
        return reactiveService.mergeMultipleFlux();
    }

    /**
     * Error handling
     * GET /api/concurrency/reactive/error-handling?shouldFail=true
     */
    @GetMapping("/reactive/error-handling")
    public Mono<String> reactiveErrorHandling(
            @RequestParam(defaultValue = "false") boolean shouldFail
    ) {
        log.info("Reactive error handling, shouldFail: {}", shouldFail);
        return reactiveService.withErrorHandling(shouldFail);
    }

    /**
     * Retry demo
     * GET /api/concurrency/reactive/retry?maxAttempts=3
     */
    @GetMapping("/reactive/retry")
    public Mono<String> reactiveRetry(
            @RequestParam(defaultValue = "3") int maxAttempts
    ) {
        log.info("Reactive retry with {} max attempts", maxAttempts);
        return reactiveService.withRetry(maxAttempts);
    }

    /**
     * Backpressure demo
     * GET /api/concurrency/reactive/backpressure?elements=100
     */
    @GetMapping(value = "/reactive/backpressure", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> reactiveBackpressure(
            @RequestParam(defaultValue = "100") int elements
    ) {
        log.info("Backpressure demo with {} elements", elements);
        return reactiveService.withBackpressure(elements);
    }

    /**
     * Schedulers demo
     * GET /api/concurrency/reactive/schedulers
     */
    @GetMapping("/reactive/schedulers")
    public Mono<String> reactiveSchedulers() {
        log.info("Demonstrating different schedulers");
        return reactiveService.demonstrateSchedulers();
    }

    // ============ PARALLEL STREAMS ============

    /**
     * Compare Sequential vs Parallel
     * GET /api/concurrency/parallel-streams/compare?elements=1000
     */
    @GetMapping("/parallel-streams/compare")
    public Map<String, PerformanceMetrics> parallelStreamsCompare(
            @RequestParam(defaultValue = "1000") int elements
    ) {
        log.info("Comparing sequential vs parallel with {} elements", elements);
        return parallelStreamService.compareSequentialVsParallel(elements);
    }

    /**
     * Simple parallel processing
     * GET /api/concurrency/parallel-streams/simple?elements=100
     */
    @GetMapping("/parallel-streams/simple")
    public ConcurrentTasksResult<String> parallelStreamsSimple(
            @RequestParam(defaultValue = "100") int elements
    ) {
        log.info("Simple parallel stream processing with {} elements", elements);
        return parallelStreamService.simpleParallelProcessing(elements);
    }

    /**
     * Parallel reductions
     * GET /api/concurrency/parallel-streams/reductions?elements=10000
     */
    @GetMapping("/parallel-streams/reductions")
    public Map<String, Object> parallelStreamsReductions(
            @RequestParam(defaultValue = "10000") int elements
    ) {
        log.info("Parallel reductions with {} elements", elements);
        return parallelStreamService.parallelReductions(elements);
    }

    /**
     * Grouping and partitioning
     * GET /api/concurrency/parallel-streams/grouping?elements=1000
     */
    @GetMapping("/parallel-streams/grouping")
    public Map<String, Object> parallelStreamsGrouping(
            @RequestParam(defaultValue = "1000") int elements
    ) {
        log.info("Parallel grouping with {} elements", elements);
        return parallelStreamService.parallelGroupingAndPartitioning(elements);
    }

    /**
     * Thread safety demo
     * GET /api/concurrency/parallel-streams/thread-safety?elements=1000
     */
    @GetMapping("/parallel-streams/thread-safety")
    public Map<String, Object> parallelStreamsThreadSafety(
            @RequestParam(defaultValue = "1000") int elements
    ) {
        log.info("Thread safety demo with {} elements", elements);
        return parallelStreamService.threadSafetyDemo(elements);
    }

    /**
     * When NOT to use parallel
     * GET /api/concurrency/parallel-streams/when-not-to-use?elements=100
     */
    @GetMapping("/parallel-streams/when-not-to-use")
    public Map<String, PerformanceMetrics> parallelStreamsWhenNotToUse(
            @RequestParam(defaultValue = "100") int elements
    ) {
        log.info("Demonstrating when NOT to use parallel streams");
        return parallelStreamService.whenNotToUseParallel(elements);
    }

    /**
     * Find operations
     * GET /api/concurrency/parallel-streams/find?elements=10000
     */
    @GetMapping("/parallel-streams/find")
    public Map<String, Object> parallelStreamsFind(
            @RequestParam(defaultValue = "10000") int elements
    ) {
        log.info("Find operations with {} elements", elements);
        return parallelStreamService.findOperations(elements);
    }

    /**
     * Short-circuit operations
     * GET /api/concurrency/parallel-streams/short-circuit?elements=10000
     */
    @GetMapping("/parallel-streams/short-circuit")
    public Map<String, Object> parallelStreamsShortCircuit(
            @RequestParam(defaultValue = "10000") int elements
    ) {
        log.info("Short-circuit operations with {} elements", elements);
        return parallelStreamService.shortCircuitOperations(elements);
    }

    // ============ BENCHMARK ============

    /**
     * Run comprehensive benchmark
     * GET /api/concurrency/benchmark/comprehensive?tasks=100
     */
    @GetMapping("/benchmark/comprehensive")
    public Map<String, PerformanceMetrics> benchmarkComprehensive(
            @RequestParam(defaultValue = "100") int tasks
    ) {
        log.info("Running comprehensive benchmark with {} tasks", tasks);
        return benchmarkService.runComprehensiveBenchmark(tasks);
    }

    /**
     * Benchmark I/O intensive operations
     * GET /api/concurrency/benchmark/io-intensive?tasks=200
     */
    @GetMapping("/benchmark/io-intensive")
    public Map<String, PerformanceMetrics> benchmarkIOIntensive(
            @RequestParam(defaultValue = "200") int tasks
    ) {
        log.info("Running I/O intensive benchmark with {} tasks", tasks);
        return benchmarkService.benchmarkIOIntensive(tasks);
    }

    /**
     * Benchmark CPU intensive operations
     * GET /api/concurrency/benchmark/cpu-intensive?tasks=100
     */
    @GetMapping("/benchmark/cpu-intensive")
    public Map<String, PerformanceMetrics> benchmarkCPUIntensive(
            @RequestParam(defaultValue = "100") int tasks
    ) {
        log.info("Running CPU intensive benchmark with {} tasks", tasks);
        return benchmarkService.benchmarkCPUIntensive(tasks);
    }

    /**
     * Scalability benchmark
     * GET /api/concurrency/benchmark/scalability
     */
    @GetMapping("/benchmark/scalability")
    public Map<Integer, Map<String, PerformanceMetrics>> benchmarkScalability() {
        log.info("Running scalability benchmark");
        return benchmarkService.scalabilityBenchmark();
    }

    // ============ INFO ENDPOINT ============

    /**
     * Get information about the concurrency setup
     * GET /api/concurrency/info
     */
    @GetMapping("/info")
    public Map<String, Object> getConcurrencyInfo() {
        Runtime runtime = Runtime.getRuntime();
        Thread currentThread = Thread.currentThread();

        return Map.of(
            "javaVersion", System.getProperty("java.version"),
            "availableProcessors", runtime.availableProcessors(),
            "totalMemoryMB", runtime.totalMemory() / (1024 * 1024),
            "freeMemoryMB", runtime.freeMemory() / (1024 * 1024),
            "maxMemoryMB", runtime.maxMemory() / (1024 * 1024),
            "currentThread", Map.of(
                "name", currentThread.getName(),
                "isVirtual", currentThread.isVirtual(),
                "isDaemon", currentThread.isDaemon()
            ),
            "features", List.of(
                "Virtual Threads (Java 21+)",
                "Structured Concurrency (Java 21+ Preview)",
                "CompletableFuture",
                "Project Reactor",
                "Parallel Streams"
            )
        );
    }
}
