# Java 21 — Révolution de la concurrence

- **Virtual Threads** : `VirtualThreadDemo` exécute des tâches bloquantes via `Executors.newVirtualThreadPerTaskExecutor()`.
- **Pattern Matching for switch & Record Patterns** : `Geometry.describe()` montre la décomposition de records directement dans le `switch`.
- **Sequenced Collections** : `SequencedCollectionsDemo` illustre `getFirst()` / `getLast()`.
- **String Templates (preview)** : `StringTemplateDemo` introduit une interpolation type-safe (`--enable-preview` requis).
- **Scoped Values (preview)** : `ScopedValuesDemo` partage un contexte immuable sans `ThreadLocal`.
- **ZGC générationnel** : activez `-XX:+UseZGC -XX:+ZGenerational` pour des pauses minimales.

```java
var scope = new ScopedValuesDemo();
scope.runInScope("req-42");
```

## Ateliers concurrentiels modulaires

Le package `com.example.java21.workshop` isole chaque notion dans une classe autonome :

| Concept | Classe principale | Points clés Java 9+ |
| --- | --- | --- |
| Introduction à `CompletableFuture` | `CompletableFutureIntroduction` | `orTimeout`, `completeOnTimeout`, threads virtuels |
| Processus & `ProcessHandle` | `ProcessHandleExplorer` | Inspection, supervision et métriques CPU |
| Parallélisation asynchrone | `AsyncTaskParallelizer` | `CompletableFuture.allOf`, temps limites, exécuteur virtuel |
| TP `CompletableFuture` complet | `CityAnalyticsApp` | Agrégation de données, `ProcessHandle`, supervision de sous-processus |
| Flux réactifs (`Flow`) | `ReactiveStreamsIntroduction` | `SubmissionPublisher`, backpressure manuel |
| Debugging & optimisation | `ConcurrentDebuggingToolkit` | `ThreadMXBean`, timeouts, callbacks `whenComplete` |
| TP réactif asynchrone | `ReactiveAlertApp` | Chaîne Publisher → Processor → Subscriber, `delayedExecutor` |

Les classes utilitaires partagées (`WeatherRecord`, `CityReport`, `ComfortIndexProcessor`, etc.) résident dans `com.example.java21.workshop.support`.

### Exécuter un atelier

```bash
mvn -pl java21-concurrency -am exec:java -Dexec.mainClass=com.example.java21.workshop.CompletableFutureIntroduction
```

Remplacez `CompletableFutureIntroduction` par la classe souhaitée (`ProcessHandleExplorer`, `AsyncTaskParallelizer`, etc.).

Chaque exemple journalise les threads utilisés, les métriques de performance et les événements réactifs afin de faciliter le debugging et l'optimisation.
