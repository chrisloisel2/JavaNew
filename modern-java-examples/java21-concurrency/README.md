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
