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

## Atelier : pipeline urbain asynchrone

Le package `com.example.java21.project` propose un mini-projet complet qui couvre :

- l'orchestration d'appels distants avec `CompletableFuture` et des threads virtuels (`CompletableFutureWorkshop`),
- l'utilisation de `ProcessHandle` pour observer et superviser des sous-processus,
- la mise en place de flux réactifs (`SubmissionPublisher`, `Flow.Processor`) avec des tâches asynchrones (`ReactiveWorkshop`).

### Lancer le pipeline de `CompletableFuture`

```bash
mvn -pl java21-concurrency -am exec:java -Dexec.mainClass=com.example.java21.project.CompletableFutureWorkshop
```

### Lancer la démo réactive

```bash
mvn -pl java21-concurrency -am exec:java -Dexec.mainClass=com.example.java21.project.ReactiveWorkshop
```

Chaque exemple journalise les threads utilisés, les métriques de performance et les événements réactifs afin de faciliter le debugging et l'optimisation.
