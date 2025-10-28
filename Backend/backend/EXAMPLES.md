# Exemples Pratiques de Multithreading en Java

## 🎯 Exemples par Cas d'Usage

### 1. Application E-Commerce: Traitement de Commande

#### Scénario
Lors du placement d'une commande, nous devons:
1. Vérifier l'inventaire
2. Traiter le paiement
3. Calculer les frais de livraison
4. Envoyer des notifications

**Toutes ces opérations sont indépendantes et peuvent s'exécuter en parallèle!**

#### Solution avec Structured Concurrency

```java
@Service
public class OrderService {

    public Order processOrder(OrderRequest request) throws Exception {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // Lancer toutes les tâches en parallèle
            var inventoryCheck = scope.fork(() ->
                inventoryService.checkAvailability(request.getItems())
            );

            var paymentProcess = scope.fork(() ->
                paymentService.processPayment(request.getPaymentInfo())
            );

            var shippingCalculation = scope.fork(() ->
                shippingService.calculateCost(request.getAddress())
            );

            // Attendre que toutes se terminent
            scope.join();
            scope.throwIfFailed(); // Lance une exception si une a échoué

            // Toutes les tâches ont réussi, créer la commande
            return Order.builder()
                .items(request.getItems())
                .inventoryConfirmation(inventoryCheck.get())
                .paymentConfirmation(paymentProcess.get())
                .shippingCost(shippingCalculation.get())
                .build();
        }
    }
}
```

**Avantages:**
- ✅ Si le paiement échoue, les autres tâches sont annulées automatiquement
- ✅ Pas de thread leak
- ✅ Code lisible et maintenable

#### Alternative avec CompletableFuture

```java
public CompletableFuture<Order> processOrderAsync(OrderRequest request) {
    CompletableFuture<InventoryResult> inventory =
        CompletableFuture.supplyAsync(() ->
            inventoryService.checkAvailability(request.getItems())
        );

    CompletableFuture<PaymentResult> payment =
        CompletableFuture.supplyAsync(() ->
            paymentService.processPayment(request.getPaymentInfo())
        );

    CompletableFuture<ShippingCost> shipping =
        CompletableFuture.supplyAsync(() ->
            shippingService.calculateCost(request.getAddress())
        );

    return CompletableFuture.allOf(inventory, payment, shipping)
        .thenApply(_ -> Order.builder()
            .items(request.getItems())
            .inventoryConfirmation(inventory.join())
            .paymentConfirmation(payment.join())
            .shippingCost(shipping.join())
            .build()
        );
}
```

### 2. Agrégation de Données: Dashboard Utilisateur

#### Scénario
Un dashboard affiche des données provenant de 5 services différents:
- Profil utilisateur
- Statistiques d'activité
- Notifications récentes
- Recommandations
- Solde du compte

#### Solution avec Project Reactor (Reactive)

```java
@Service
public class DashboardService {

    public Mono<Dashboard> getUserDashboard(String userId) {
        // Lancer tous les appels en parallèle
        Mono<UserProfile> profile = userService.getProfile(userId);
        Mono<ActivityStats> stats = activityService.getStats(userId);
        Mono<List<Notification>> notifications = notificationService.getRecent(userId);
        Mono<List<Product>> recommendations = recommendationService.get(userId);
        Mono<BigDecimal> balance = accountService.getBalance(userId);

        // Combiner tous les résultats
        return Mono.zip(profile, stats, notifications, recommendations, balance)
            .map(tuple -> Dashboard.builder()
                .profile(tuple.getT1())
                .stats(tuple.getT2())
                .notifications(tuple.getT3())
                .recommendations(tuple.getT4())
                .balance(tuple.getT5())
                .build()
            )
            .timeout(Duration.ofSeconds(5)) // Timeout global
            .onErrorResume(ex -> {
                log.error("Error loading dashboard", ex);
                return Mono.just(Dashboard.empty());
            });
    }
}
```

**Avantages:**
- ✅ Non-bloquant
- ✅ Backpressure automatique
- ✅ Timeout et retry faciles

### 3. Traitement de Fichiers: Import de Données

#### Scénario
Importer un fichier CSV de 1 million de lignes:
1. Lire le fichier
2. Valider chaque ligne
3. Transformer les données
4. Sauvegarder en base de données

#### Solution avec Parallel Streams

```java
@Service
public class ImportService {

    public ImportResult importCsvFile(Path filePath) {
        try (Stream<String> lines = Files.lines(filePath)) {
            List<ImportedRecord> results = lines
                .skip(1) // Ignorer l'en-tête
                .parallel() // Traitement parallèle
                .map(this::parseLine)
                .filter(Objects::nonNull)
                .map(this::validateAndTransform)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

            // Sauvegarde par batch
            batchSave(results);

            return ImportResult.builder()
                .totalProcessed(results.size())
                .build();
        }
    }

    private ImportedRecord parseLine(String line) {
        try {
            String[] parts = line.split(",");
            return new ImportedRecord(parts);
        } catch (Exception e) {
            log.warn("Failed to parse line: {}", line);
            return null;
        }
    }

    private Optional<ImportedRecord> validateAndTransform(ImportedRecord record) {
        // Validation business
        if (record.isValid()) {
            record.transform();
            return Optional.of(record);
        }
        return Optional.empty();
    }

    private void batchSave(List<ImportedRecord> records) {
        // Sauvegarder par lots de 1000
        Lists.partition(records, 1000).forEach(batch ->
            repository.saveAll(batch)
        );
    }
}
```

### 4. API Gateway: Appel à Plusieurs Services

#### Scénario
Un endpoint d'API Gateway doit appeler 3 microservices et agréger les réponses.

#### Solution avec CompletableFuture et Virtual Threads

```java
@RestController
@RequestMapping("/api/gateway")
public class GatewayController {

    private final Executor virtualExecutor =
        Executors.newVirtualThreadPerTaskExecutor();

    @GetMapping("/user-data/{userId}")
    public CompletableFuture<AggregatedResponse> getUserData(@PathVariable String userId) {
        // Appels parallèles à 3 services
        CompletableFuture<UserInfo> userInfo =
            CompletableFuture.supplyAsync(() ->
                callService("user-service", userId), virtualExecutor
            );

        CompletableFuture<OrderHistory> orders =
            CompletableFuture.supplyAsync(() ->
                callService("order-service", userId), virtualExecutor
            );

        CompletableFuture<PreferenceData> preferences =
            CompletableFuture.supplyAsync(() ->
                callService("preference-service", userId), virtualExecutor
            );

        // Combiner avec timeout individuels
        return CompletableFuture.allOf(
                userInfo.orTimeout(2, TimeUnit.SECONDS),
                orders.orTimeout(5, TimeUnit.SECONDS),
                preferences.orTimeout(3, TimeUnit.SECONDS)
            )
            .thenApply(_ -> new AggregatedResponse(
                userInfo.join(),
                orders.join(),
                preferences.join()
            ))
            .exceptionally(ex -> {
                // Gestion d'erreur avec valeurs par défaut
                return AggregatedResponse.withDefaults(
                    userInfo.getNow(null),
                    orders.getNow(null),
                    preferences.getNow(null)
                );
            });
    }

    private <T> T callService(String serviceName, String userId) {
        // Simulation d'appel HTTP
        // En réalité: restTemplate.getForObject(...)
        return (T) httpClient.get(serviceName + "/users/" + userId);
    }
}
```

### 5. Rate Limiting avec Virtual Threads

#### Scénario
Limiter le nombre de requêtes concurrentes vers une API externe.

#### Solution avec Semaphore et Virtual Threads

```java
@Service
public class RateLimitedApiService {

    private final Semaphore rateLimiter = new Semaphore(10); // Max 10 concurrent
    private final Executor virtualExecutor =
        Executors.newVirtualThreadPerTaskExecutor();

    public CompletableFuture<ApiResponse> callExternalApi(Request request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Attendre un slot
                rateLimiter.acquire();
                try {
                    // Faire l'appel API
                    return externalApiClient.call(request);
                } finally {
                    // Libérer le slot
                    rateLimiter.release();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }, virtualExecutor);
    }

    // Appel de masse
    public CompletableFuture<List<ApiResponse>> callBatch(List<Request> requests) {
        List<CompletableFuture<ApiResponse>> futures = requests.stream()
            .map(this::callExternalApi)
            .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(_ -> futures.stream()
                .map(CompletableFuture::join)
                .toList()
            );
    }
}
```

### 6. Websocket: Notifications en Temps Réel

#### Scénario
Envoyer des notifications en temps réel à des milliers d'utilisateurs connectés.

#### Solution avec Flux (Reactor)

```java
@Service
public class NotificationService {

    private final Map<String, Sinks.Many<Notification>> userSinks =
        new ConcurrentHashMap<>();

    // S'abonner aux notifications d'un utilisateur
    public Flux<Notification> subscribeToNotifications(String userId) {
        Sinks.Many<Notification> sink = Sinks.many()
            .multicast()
            .onBackpressureBuffer();

        userSinks.put(userId, sink);

        return sink.asFlux()
            .doOnCancel(() -> userSinks.remove(userId))
            .doOnComplete(() -> userSinks.remove(userId));
    }

    // Envoyer une notification à un utilisateur
    public void sendNotification(String userId, Notification notification) {
        Sinks.Many<Notification> sink = userSinks.get(userId);
        if (sink != null) {
            sink.tryEmitNext(notification);
        }
    }

    // Broadcast à tous les utilisateurs connectés
    public void broadcastAll(Notification notification) {
        userSinks.values().parallelStream()
            .forEach(sink -> sink.tryEmitNext(notification));
    }

    // Envoyer à un groupe d'utilisateurs
    public void sendToGroup(List<String> userIds, Notification notification) {
        Flux.fromIterable(userIds)
            .parallel()
            .runOn(Schedulers.parallel())
            .doOnNext(userId -> sendNotification(userId, notification))
            .sequential()
            .subscribe();
    }
}
```

### 7. Cache Warmer: Réchauffement de Cache au Démarrage

#### Scénario
Au démarrage de l'application, charger les données fréquemment accédées en cache.

#### Solution avec @EventListener et Virtual Threads

```java
@Component
public class CacheWarmer {

    private static final Logger log = LoggerFactory.getLogger(CacheWarmer.class);

    @EventListener(ApplicationReadyEvent.class)
    public void warmupCache() {
        log.info("Starting cache warmup...");

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<Void>> futures = List.of(
                CompletableFuture.runAsync(this::warmupUserCache, executor),
                CompletableFuture.runAsync(this::warmupProductCache, executor),
                CompletableFuture.runAsync(this::warmupCategoryCache, executor),
                CompletableFuture.runAsync(this::warmupConfigCache, executor)
            );

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> log.info("Cache warmup completed!"))
                .exceptionally(ex -> {
                    log.error("Cache warmup failed", ex);
                    return null;
                })
                .join();
        }
    }

    private void warmupUserCache() {
        List<User> activeUsers = userRepository.findActiveUsers();
        activeUsers.forEach(user -> cacheManager.put("user:" + user.getId(), user));
        log.info("Warmed up {} users", activeUsers.size());
    }

    private void warmupProductCache() {
        List<Product> popularProducts = productRepository.findPopular(1000);
        popularProducts.forEach(p -> cacheManager.put("product:" + p.getId(), p));
        log.info("Warmed up {} products", popularProducts.size());
    }

    // ... autres méthodes de warmup
}
```

### 8. Batch Processing: Traitement Nocturne

#### Scénario
Traiter 1 million d'enregistrements chaque nuit (rapports, calculs, etc.).

#### Solution avec Parallel Streams et Partition

```java
@Service
public class BatchProcessingService {

    @Scheduled(cron = "0 0 2 * * *") // 2h du matin
    public void processNightlyBatch() {
        log.info("Starting nightly batch processing...");

        LocalDateTime start = LocalDateTime.now();

        // Récupérer tous les IDs à traiter
        List<Long> recordIds = repository.findAllIdsToProcess();

        // Diviser en chunks de 10000
        Lists.partition(recordIds, 10000).stream()
            .parallel()
            .forEach(this::processChunk);

        Duration duration = Duration.between(start, LocalDateTime.now());
        log.info("Batch completed in {} minutes", duration.toMinutes());
    }

    private void processChunk(List<Long> ids) {
        List<Record> records = repository.findAllById(ids);

        List<ProcessedRecord> processed = records.parallelStream()
            .map(this::processRecord)
            .filter(Objects::nonNull)
            .toList();

        repository.saveAllProcessed(processed);

        log.info("Processed chunk of {} records", processed.size());
    }

    private ProcessedRecord processRecord(Record record) {
        try {
            // Logique métier complexe
            ProcessedRecord result = businessLogic.process(record);
            result.setProcessedAt(LocalDateTime.now());
            return result;
        } catch (Exception e) {
            log.error("Failed to process record {}", record.getId(), e);
            return null;
        }
    }
}
```

## 🎓 Anti-Patterns à Éviter

### ❌ 1. Bloquer dans un Reactive Stream

```java
// MAUVAIS
Mono.fromCallable(() -> {
    Thread.sleep(1000); // ❌ Bloque le thread
    return result;
});

// BON
Mono.delay(Duration.ofSeconds(1))
    .map(_ -> result);
```

### ❌ 2. Utiliser Parallel Streams pour I/O

```java
// MAUVAIS - ForkJoinPool n'est pas optimal pour I/O
List<Data> results = urls.parallelStream()
    .map(url -> httpClient.get(url)) // ❌ I/O bloquant
    .toList();

// BON - Virtual Threads pour I/O
List<CompletableFuture<Data>> futures = urls.stream()
    .map(url -> CompletableFuture.supplyAsync(
        () -> httpClient.get(url),
        virtualExecutor
    ))
    .toList();
```

### ❌ 3. Oublier les Timeouts

```java
// MAUVAIS - Peut bloquer indéfiniment
CompletableFuture<Data> future = service.getData();
Data result = future.join(); // ❌ Pas de timeout

// BON
Data result = future
    .orTimeout(5, TimeUnit.SECONDS)
    .exceptionally(ex -> defaultValue)
    .join();
```

## 📊 Tableau Récapitulatif: Quoi Utiliser Quand?

| Cas d'Usage | Recommandation | Alternative |
|-------------|----------------|-------------|
| **I/O intensif** (DB, réseau) | Virtual Threads / Structured Concurrency | Reactor |
| **CPU intensif** | Parallel Streams | Reactor avec parallel() |
| **Composition d'ops async** | CompletableFuture | Reactor |
| **Streams en temps réel** | Reactor (Flux) | - |
| **Beaucoup de tâches simples** | Virtual Threads | Parallel Streams |
| **Timeout critique** | CompletableFuture.orTimeout() | Reactor timeout() |
| **Backpressure nécessaire** | Reactor | - |
| **Code legacy** | CompletableFuture | - |

---

**Tip:** Testez toujours les performances avec des benchmarks réels avant de choisir une approche!
