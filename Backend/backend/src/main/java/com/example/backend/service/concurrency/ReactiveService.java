package com.example.backend.service.concurrency;

import com.example.backend.dto.async.AsyncTaskResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Service démontrant l'utilisation de Project Reactor
 *
 * Concepts clés:
 * - Mono<T>: 0 ou 1 élément asynchrone
 * - Flux<T>: 0 à N éléments asynchrones (stream réactif)
 * - Backpressure: gestion de la pression entre producteur et consommateur
 * - Schedulers: gestion des threads pour les opérations
 */
@Service
@Slf4j
public class ReactiveService {

    /**
     * Mono simple - opération asynchrone retournant un seul résultat
     */
    public Mono<AsyncTaskResult<String>> simpleMonoOperation(String input) {
        LocalDateTime startTime = LocalDateTime.now();

        return Mono.fromCallable(() -> {
            Thread.sleep(200);
            return "Traité: " + input;
        })
        .subscribeOn(Schedulers.boundedElastic()) // Thread pool pour I/O
        .map(result -> AsyncTaskResult.of(result, startTime, "Mono with boundedElastic"))
        .doOnNext(result -> log.info("Mono terminé sur thread: {}", Thread.currentThread().getName()));
    }

    /**
     * Flux simple - stream d'éléments émis de manière asynchrone
     */
    public Flux<String> simpleFluxOperation(int count) {
        return Flux.range(1, count)
            .delayElements(Duration.ofMillis(100)) // Délai entre chaque élément
            .map(i -> "Élément " + i)
            .doOnNext(item -> log.info("Émis: {} sur thread: {}", item, Thread.currentThread().getName()));
    }

    /**
     * Traitement parallèle avec Flux
     * Utilise plusieurs threads pour traiter les éléments en parallèle
     */
    public Mono<List<String>> parallelFluxProcessing(int numberOfElements) {
        return Flux.range(1, numberOfElements)
            .parallel() // Divise le flux en rails parallèles
            .runOn(Schedulers.parallel()) // Thread pool pour CPU-bound
            .map(i -> {
                // Simule un traitement CPU intensif
                sleep(ThreadLocalRandom.current().nextInt(50, 150));
                return "Traité en parallèle: " + i + " sur " + Thread.currentThread().getName();
            })
            .sequential() // Regroupe les rails en un seul flux
            .collectList(); // Collecte tous les résultats
    }

    /**
     * FlatMap - composition asynchrone avec aplatissement
     * Chaque élément déclenche une opération async qui retourne un Publisher
     */
    public Flux<String> flatMapOperation(List<String> inputs) {
        return Flux.fromIterable(inputs)
            .flatMap(input ->
                Mono.fromCallable(() -> {
                    sleep(ThreadLocalRandom.current().nextInt(100, 300));
                    return input.toUpperCase();
                })
                .subscribeOn(Schedulers.boundedElastic())
            )
            .doOnNext(result -> log.info("FlatMap résultat: {}", result));
    }

    /**
     * ConcatMap - composition asynchrone séquentielle
     * Contrairement à flatMap, concatMap préserve l'ordre
     */
    public Flux<String> concatMapOperation(List<String> inputs) {
        return Flux.fromIterable(inputs)
            .concatMap(input ->
                Mono.fromCallable(() -> {
                    sleep(100);
                    return input.toUpperCase();
                })
                .subscribeOn(Schedulers.boundedElastic())
            )
            .doOnNext(result -> log.info("ConcatMap résultat: {}", result));
    }

    /**
     * Zip - combine plusieurs Monos en parallèle
     * Attend que tous les Monos se terminent et combine les résultats
     */
    public Mono<AsyncTaskResult<String>> zipMultipleMonos(String input1, String input2, String input3) {
        LocalDateTime startTime = LocalDateTime.now();

        Mono<String> mono1 = Mono.fromCallable(() -> {
            sleep(200);
            return "Résultat 1: " + input1;
        }).subscribeOn(Schedulers.boundedElastic());

        Mono<String> mono2 = Mono.fromCallable(() -> {
            sleep(150);
            return "Résultat 2: " + input2;
        }).subscribeOn(Schedulers.boundedElastic());

        Mono<String> mono3 = Mono.fromCallable(() -> {
            sleep(100);
            return "Résultat 3: " + input3;
        }).subscribeOn(Schedulers.boundedElastic());

        return Mono.zip(mono1, mono2, mono3)
            .map(tuple -> {
                String combined = tuple.getT1() + ", " + tuple.getT2() + ", " + tuple.getT3();
                return AsyncTaskResult.of(combined, startTime, "Mono.zip");
            });
    }

    /**
     * Merge - fusionne plusieurs flux sans attendre
     * Les éléments sont émis dès qu'ils sont disponibles
     */
    public Flux<String> mergeMultipleFlux() {
        Flux<String> flux1 = Flux.just("A", "B", "C")
            .delayElements(Duration.ofMillis(100));

        Flux<String> flux2 = Flux.just("1", "2", "3")
            .delayElements(Duration.ofMillis(150));

        Flux<String> flux3 = Flux.just("X", "Y", "Z")
            .delayElements(Duration.ofMillis(80));

        return Flux.merge(flux1, flux2, flux3)
            .doOnNext(item -> log.info("Merge émit: {}", item));
    }

    /**
     * Gestion d'erreurs avec onErrorResume
     */
    public Mono<String> withErrorHandling(boolean shouldFail) {
        return Mono.fromCallable(() -> {
            sleep(100);
            if (shouldFail) {
                throw new RuntimeException("Erreur simulée!");
            }
            return "Succès";
        })
        .subscribeOn(Schedulers.boundedElastic())
        .onErrorResume(error -> {
            log.error("Erreur capturée: {}", error.getMessage());
            return Mono.just("Erreur récupérée: " + error.getMessage());
        });
    }

    /**
     * Retry avec backoff exponentiel
     */
    public Mono<String> withRetry(int maxAttempts) {
        return Mono.fromCallable(() -> {
            sleep(100);
            // 50% de chances d'échouer
            if (ThreadLocalRandom.current().nextBoolean()) {
                throw new RuntimeException("Échec temporaire");
            }
            return "Succès après retry";
        })
        .subscribeOn(Schedulers.boundedElastic())
        .doOnError(e -> log.warn("Tentative échouée: {}", e.getMessage()))
        .retry(maxAttempts);
    }

    /**
     * Timeout sur opération réactive
     */
    public Mono<String> withTimeout(int delayMs, int timeoutMs) {
        return Mono.fromCallable(() -> {
            sleep(delayMs);
            return "Opération terminée";
        })
        .subscribeOn(Schedulers.boundedElastic())
        .timeout(Duration.ofMillis(timeoutMs))
        .onErrorResume(error -> Mono.just("Timeout dépassé!"));
    }

    /**
     * Cache - mémorise le résultat d'une opération coûteuse
     */
    public Mono<String> cachedOperation(String input) {
        return Mono.fromCallable(() -> {
            sleep(1000); // Opération très coûteuse
            return "Résultat coûteux pour: " + input;
        })
        .subscribeOn(Schedulers.boundedElastic())
        .cache(Duration.ofMinutes(5)); // Cache pendant 5 minutes
    }

    /**
     * Backpressure - gestion de la pression
     * onBackpressureBuffer: met en buffer les éléments
     */
    public Flux<String> withBackpressure(int numberOfElements) {
        return Flux.range(1, numberOfElements)
            .delayElements(Duration.ofMillis(10)) // Production rapide
            .onBackpressureBuffer(100) // Buffer de 100 éléments
            .map(i -> "Élément " + i + " avec backpressure")
            .doOnNext(item -> {
                sleep(50); // Consommation lente
                log.info("Consommé: {}", item);
            });
    }

    /**
     * Flux infini avec window
     * Découpe un flux en fenêtres temporelles
     */
    public Flux<List<Integer>> windowedFlux(Duration windowDuration) {
        return Flux.interval(Duration.ofMillis(100))
            .take(50) // Limite pour l'exemple
            .map(Long::intValue)
            .window(windowDuration)
            .flatMap(Flux::collectList)
            .doOnNext(window -> log.info("Fenêtre de {} éléments", window.size()));
    }

    /**
     * Buffer - regroupe les éléments par lot
     */
    public Flux<List<Integer>> bufferedFlux(int bufferSize) {
        return Flux.range(1, 100)
            .buffer(bufferSize)
            .doOnNext(batch -> log.info("Batch de {} éléments: {}", batch.size(), batch));
    }

    /**
     * Context - propagation de contexte réactif
     * Utile pour tracer les requêtes ou gérer des contextes utilisateur
     */
    public Mono<String> withContext(String userId) {
        return Mono.deferContextual(ctx -> {
            String user = ctx.get("userId");
            return Mono.just("Traitement pour l'utilisateur: " + user);
        })
        .contextWrite(context -> context.put("userId", userId))
        .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * SwitchIfEmpty - fournit une alternative si le flux est vide
     */
    public Mono<String> withFallback(boolean isEmpty) {
        return Mono.<String>defer(() -> {
            if (isEmpty) {
                return Mono.empty();
            }
            return Mono.just("Résultat trouvé");
        })
        .switchIfEmpty(Mono.just("Valeur par défaut"))
        .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Démonstration des différents schedulers
     */
    public Mono<String> demonstrateSchedulers() {
        return Mono.just("Démo schedulers")
            // immediate(): exécute immédiatement sur le thread courant
            .publishOn(Schedulers.immediate())
            .doOnNext(s -> log.info("immediate: {}", Thread.currentThread().getName()))

            // parallel(): pool de threads pour CPU-bound (nombre de coeurs)
            .publishOn(Schedulers.parallel())
            .doOnNext(s -> log.info("parallel: {}", Thread.currentThread().getName()))

            // boundedElastic(): pool élastique pour I/O-bound
            .publishOn(Schedulers.boundedElastic())
            .doOnNext(s -> log.info("boundedElastic: {}", Thread.currentThread().getName()))

            .map(s -> "Schedulers démontrés");
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
