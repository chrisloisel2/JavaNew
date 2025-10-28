# Guide Complet du Multithreading Moderne en Java

Ce projet démontre **toutes les approches modernes de multithreading en Java**, avec un focus particulier sur les nouveautés de Java 21, 22, 23, et 25.

## 🚀 Fonctionnalités Implémentées

### 1. **Virtual Threads (Project Loom - Java 21+)**
- Threads légers gérés par la JVM
- Peuvent créer des millions de threads avec peu de ressources
- Idéal pour les opérations I/O-bound

**Configuration:** `VirtualThreadConfig.java`
```java
Executors.newVirtualThreadPerTaskExecutor()
```

### 2. **Structured Concurrency (Java 21+ Preview)**
- Garantit que toutes les sous-tâches se terminent avant la tâche principale
- Annulation automatique en cas d'échec
- Prévention des "thread leaks"

**Service:** `StructuredConcurrencyService.java`

**Patterns implémentés:**
- `ShutdownOnFailure`: Annule tout si une tâche échoue
- `ShutdownOnSuccess`: S'arrête à la première réussite
- Tâches hiérarchiques

### 3. **CompletableFuture (Java 8+, amélioré en 9+)**
- API fluide pour la programmation asynchrone
- Composition et chaînage d'opérations
- Gestion d'erreurs avancée

**Service:** `CompletableFutureService.java`

**Patterns implémentés:**
- `thenCompose`: Chaînage séquentiel
- `thenCombine`: Combinaison parallèle
- `allOf`: Attendre toutes les tâches
- `anyOf`: Race condition
- `orTimeout`: Timeout avec Java 9+
- `completeOnTimeout`: Valeur par défaut
- Fan-out / Fan-in pattern

### 4. **Project Reactor (Reactive Programming)**
- Programmation réactive avec Mono et Flux
- Backpressure intégré
- Schedulers pour différents types de workload

**Service:** `ReactiveService.java`

**Concepts démontrés:**
- `Mono<T>`: 0 ou 1 élément
- `Flux<T>`: 0 à N éléments
- `flatMap`, `concatMap`, `zip`, `merge`
- Gestion d'erreurs et retry
- Schedulers: `parallel()`, `boundedElastic()`, `immediate()`
- Backpressure et windowing

### 5. **Parallel Streams (Java 8+)**
- Traitement parallèle automatique avec ForkJoinPool
- Bon pour CPU-bound avec petites opérations

**Service:** `ParallelStreamService.java`

**Démos:**
- Comparaison séquentiel vs parallèle
- Réductions parallèles (sum, average, etc.)
- Grouping et partitioning
- Thread-safety
- Quand NE PAS utiliser parallel streams

### 6. **Benchmarks Complets**
- Comparaison de performances entre toutes les approches
- Tests I/O-bound vs CPU-bound
- Tests de scalabilité

**Service:** `BenchmarkService.java`

## 📋 Prérequis

- **Java 25** (ou 21+ minimum pour Virtual Threads et Structured Concurrency)
- **Maven 3.8+**
- **Spring Boot 3.5.7**

## 🔧 Configuration

### Activation des Preview Features

Le projet nécessite l'activation des preview features pour Structured Concurrency:

**pom.xml:**
```xml
<compilerArgs>
    <arg>--enable-preview</arg>
</compilerArgs>
```

**Pour exécuter:**
```bash
mvn spring-boot:run
# ou
java --enable-preview -jar backend.jar
```

### Configuration des Executors

**AsyncExecutorConfig.java** fournit plusieurs pools de threads:
- `cpuIntensiveExecutor`: Pour calculs CPU (taille = nb de coeurs)
- `ioIntensiveExecutor`: Pour I/O avec Virtual Threads
- `traditionalExecutor`: Pool classique pour compatibilité

## 🎯 API Endpoints

### Structured Concurrency

```http
GET /api/concurrency/structured-concurrency/shutdown-on-failure?tasks=5
GET /api/concurrency/structured-concurrency/shutdown-on-success?tasks=10
GET /api/concurrency/structured-concurrency/hierarchical
GET /api/concurrency/structured-concurrency/compare?tasks=10
```

### CompletableFuture

```http
GET /api/concurrency/completable-future/simple?input=test
GET /api/concurrency/completable-future/chained?data=myData
GET /api/concurrency/completable-future/parallel?tasks=10
GET /api/concurrency/completable-future/race?tasks=10
GET /api/concurrency/completable-future/combine?input1=A&input2=B
GET /api/concurrency/completable-future/error-handling?shouldFail=true
POST /api/concurrency/completable-future/fan-out-fan-in
GET /api/concurrency/completable-future/timeout?delay=100&timeout=200
```

### Reactive (Project Reactor)

```http
GET /api/concurrency/reactive/mono?input=test
GET /api/concurrency/reactive/flux?count=10  # Server-Sent Events
GET /api/concurrency/reactive/parallel?elements=20
POST /api/concurrency/reactive/flatmap
GET /api/concurrency/reactive/zip?input1=A&input2=B&input3=C
GET /api/concurrency/reactive/merge  # Server-Sent Events
GET /api/concurrency/reactive/error-handling?shouldFail=true
GET /api/concurrency/reactive/retry?maxAttempts=3
GET /api/concurrency/reactive/backpressure?elements=100
GET /api/concurrency/reactive/schedulers
```

### Parallel Streams

```http
GET /api/concurrency/parallel-streams/compare?elements=1000
GET /api/concurrency/parallel-streams/simple?elements=100
GET /api/concurrency/parallel-streams/reductions?elements=10000
GET /api/concurrency/parallel-streams/grouping?elements=1000
GET /api/concurrency/parallel-streams/thread-safety?elements=1000
GET /api/concurrency/parallel-streams/when-not-to-use?elements=100
GET /api/concurrency/parallel-streams/find?elements=10000
GET /api/concurrency/parallel-streams/short-circuit?elements=10000
```

### Benchmarks

```http
GET /api/concurrency/benchmark/comprehensive?tasks=100
GET /api/concurrency/benchmark/io-intensive?tasks=200
GET /api/concurrency/benchmark/cpu-intensive?tasks=100
GET /api/concurrency/benchmark/scalability
```

### Info

```http
GET /api/concurrency/info
```

## 🏃 Démarrage Rapide

### 1. Compiler le projet
```bash
mvn clean install
```

### 2. Lancer l'application
```bash
mvn spring-boot:run
```

### 3. Tester les endpoints

**Info système:**
```bash
curl http://localhost:8080/api/concurrency/info
```

**Test Virtual Threads:**
```bash
curl http://localhost:8080/api/concurrency/structured-concurrency/shutdown-on-failure?tasks=5
```

**Benchmark complet:**
```bash
curl http://localhost:8080/api/concurrency/benchmark/comprehensive?tasks=100
```

**Stream réactif (SSE):**
```bash
curl http://localhost:8080/api/concurrency/reactive/flux?count=20
```

## 📊 Comparaison des Approches

### Virtual Threads vs Traditional Threads

| Caractéristique | Virtual Threads | Platform Threads |
|----------------|-----------------|------------------|
| Mémoire par thread | ~1 KB | ~1 MB |
| Nombre max pratique | Millions | Milliers |
| Scheduler | JVM | OS |
| Idéal pour | I/O-bound | CPU-bound |
| Disponibilité | Java 21+ | Toutes versions |

### Quand utiliser quoi?

**Virtual Threads / Structured Concurrency:**
- ✅ Opérations I/O intensives (DB, réseau, fichiers)
- ✅ Beaucoup de tâches concurrentes (>10000)
- ✅ Code impératif simple
- ❌ Calculs CPU intensifs purs

**CompletableFuture:**
- ✅ Composition d'opérations asynchrones
- ✅ API fluide et expressive
- ✅ Compatible avec code existant
- ✅ Gestion d'erreurs fine

**Project Reactor:**
- ✅ Streams de données continus
- ✅ Backpressure nécessaire
- ✅ Intégration Spring WebFlux
- ✅ Opérations complexes sur streams
- ❌ Courbe d'apprentissage plus raide

**Parallel Streams:**
- ✅ Calculs CPU intensifs simples
- ✅ Collections de taille moyenne (100-100000)
- ✅ Opérations stateless
- ❌ I/O operations
- ❌ Petites collections (<100)

## 🎓 Concepts Avancés

### Structured Concurrency - Pourquoi c'est important

**Avant (problèmes):**
```java
Thread t1 = Thread.startVirtualThread(() -> task1());
Thread t2 = Thread.startVirtualThread(() -> task2());
// ⚠️ Que se passe-t-il si on oublie de join?
// ⚠️ Que se passe-t-il si task1 échoue?
// ⚠️ Comment annuler les tâches en cours?
```

**Après (avec Structured Concurrency):**
```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    var t1 = scope.fork(() -> task1());
    var t2 = scope.fork(() -> task2());

    scope.join();           // ✅ Garantit l'attente
    scope.throwIfFailed();  // ✅ Gestion d'erreur automatique

    return t1.get();        // ✅ Ressources libérées automatiquement
}
```

### CompletableFuture - Patterns Essentiels

**Sequential Composition:**
```java
CompletableFuture.supplyAsync(() -> getData())
    .thenCompose(data -> CompletableFuture.supplyAsync(() -> validate(data)))
    .thenCompose(valid -> CompletableFuture.supplyAsync(() -> enrich(valid)))
    .thenAccept(result -> save(result));
```

**Parallel Composition:**
```java
CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> task1());
CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> task2());
CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> task3());

CompletableFuture.allOf(f1, f2, f3)
    .thenRun(() -> process(f1.join(), f2.join(), f3.join()));
```

### Reactor - Backpressure

Le backpressure est crucial quand le producteur est plus rapide que le consommateur:

```java
Flux.range(1, 1000000)
    .onBackpressureBuffer(1000)  // Buffer de 1000 éléments
    .delayElements(Duration.ofMillis(10))
    .subscribe();
```

## 🔍 Bonnes Pratiques

### 1. Choisir le bon executor

```java
// I/O-bound: Virtual Threads
@Async("ioIntensiveExecutor")
public CompletableFuture<Data> fetchData() { ... }

// CPU-bound: Pool limité
@Async("cpuIntensiveExecutor")
public CompletableFuture<Result> heavyComputation() { ... }
```

### 2. Gérer les erreurs correctement

```java
// ❌ Mauvais
future.join(); // Peut lancer une exception non gérée

// ✅ Bon
future
    .exceptionally(ex -> fallbackValue)
    .thenAccept(result -> process(result));
```

### 3. Éviter les blocages dans les Reactive Streams

```java
// ❌ Mauvais
Mono.fromCallable(() -> {
    Thread.sleep(1000); // Bloque le thread
    return result;
});

// ✅ Bon
Mono.delay(Duration.ofSeconds(1))
    .map(i -> result);
```

### 4. Thread-Safety avec Parallel Streams

```java
// ❌ Non thread-safe
List<String> list = new ArrayList<>();
stream.parallel().forEach(list::add);

// ✅ Thread-safe
List<String> list = stream.parallel()
    .collect(Collectors.toList());
```

## 📈 Résultats de Performance (indicatifs)

Sur une machine avec 8 cores, 16 GB RAM, pour 1000 tâches I/O (100ms chacune):

| Approche | Temps | Threads utilisés | Mémoire |
|----------|-------|------------------|---------|
| Séquentiel | ~100s | 1 | Minimal |
| Virtual Threads | ~0.2s | 1000 | ~10 MB |
| CompletableFuture (traditional) | ~12.5s | 8-100 | ~100 MB |
| Reactor | ~0.3s | 8-100 | ~50 MB |
| Parallel Streams | ~12.5s | 8 | ~20 MB |

**Conclusion:** Virtual Threads excellent pour I/O massif!

## 🐛 Dépannage

### Erreur "Preview feature not enabled"

```bash
# Ajouter --enable-preview
java --enable-preview -jar app.jar
```

### OutOfMemoryError avec beaucoup de threads traditionnels

→ Utiliser Virtual Threads ou Reactive Programming

### CompletableFuture ne s'exécute pas

→ Vérifier que @EnableAsync est présent sur l'application

## 📚 Ressources

- [JEP 444: Virtual Threads](https://openjdk.org/jeps/444)
- [JEP 453: Structured Concurrency](https://openjdk.org/jeps/453)
- [Project Reactor Documentation](https://projectreactor.io/docs)
- [CompletableFuture Guide](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/CompletableFuture.html)

## 📝 Architecture du Projet

```
src/main/java/com/example/backend/
├── config/
│   ├── AsyncExecutorConfig.java        # Configuration des executors
│   └── VirtualThreadConfig.java        # Virtual threads pour Tomcat
├── dto/async/
│   ├── AsyncTaskResult.java            # Résultat d'une tâche async
│   ├── ConcurrentTasksResult.java      # Résultat de tâches concurrentes
│   └── PerformanceMetrics.java         # Métriques de performance
├── service/concurrency/
│   ├── StructuredConcurrencyService.java   # Structured Concurrency
│   ├── CompletableFutureService.java       # CompletableFuture patterns
│   ├── ReactiveService.java                # Project Reactor
│   ├── ParallelStreamService.java          # Parallel Streams
│   └── BenchmarkService.java               # Comparaisons
└── controller/
    └── ConcurrencyDemoController.java      # API REST
```

## 🎯 Prochaines Étapes

Pour approfondir, vous pouvez:

1. **Intégrer avec une vraie base de données** pour voir l'impact sur les performances I/O
2. **Ajouter des métriques avec Micrometer** pour monitorer en temps réel
3. **Tester avec des charges réelles** (JMeter, Gatling)
4. **Implémenter des rate limiters** avec les différentes approches
5. **Créer des circuit breakers** réactifs avec Resilience4j

## 💡 Exemples d'Usage dans un Vrai Projet

### Traitement de commandes e-commerce
```java
// Avec Structured Concurrency
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    var inventory = scope.fork(() -> checkInventory(orderId));
    var payment = scope.fork(() -> processPayment(orderId));
    var shipping = scope.fork(() -> calculateShipping(orderId));

    scope.join();
    scope.throwIfFailed();

    return createOrder(inventory.get(), payment.get(), shipping.get());
}
```

### Agrégation de données de plusieurs services
```java
// Avec Reactor
Mono<Dashboard> dashboard = Mono.zip(
    userService.getUserData(userId),
    orderService.getOrders(userId),
    analyticsService.getStats(userId)
).map(tuple -> new Dashboard(
    tuple.getT1(), // user
    tuple.getT2(), // orders
    tuple.getT3()  // stats
));
```

---

**Auteur:** Démo Java Multithreading Moderne
**Version:** Java 25 avec Spring Boot 3.5.7
**Date:** 2025
