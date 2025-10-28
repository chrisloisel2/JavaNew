# TP — Évolution des Threads en Java (8 → 21+) **avec Reactive Streams**

## 🎯 Objectifs pédagogiques

À l’issue du TP, l’étudiant sait :

* différencier **threads natifs**, **executors**, **futures**, **CompletableFuture**, **parallel streams** ;
* utiliser les nouveautés **Java 21** : **Virtual Threads**, **Structured Concurrency**, **Scoped Values** ;
* concevoir un **pipeline Reactive Streams** (API `java.util.concurrent.Flow`) avec **backpressure** ;
* combiner **Virtual Threads** et **Reactive Streams** pour des workloads I/O.

---

## 🧰 Prérequis

* JDK **21+** installé (`java -version`)
* Un IDE ou un simple terminal
* Ce fichier fourni : `ThreadEvolutionDemo.java` (un seul fichier, une fonction par notion)

> Si vous n’avez pas le fichier, demandez-le à l’enseignant ou générez-le depuis la ressource distribuée.

---

## ⚙️ Mise en route

1. **Compiler et exécuter** la démo de base :

   ```bash
   javac ThreadEvolutionDemo.java && java ThreadEvolutionDemo
   ```
2. Observer l’ordre d’exécution et les sorties de chaque section.
3. Créer un **nouveau fichier** `TpSolution.java` que vous remplirez au fil des exercices (ne modifiez pas l’original ; gardez-le comme référence).

---

## 🗺️ Plan du TP (progressif)

### Exercice 1 — Threads classiques

**But :** créer, démarrer, joindre un thread ; comprendre la latence due à `sleep()`.

* Implémentez une méthode `ex1_rawThreads()` qui :

  * crée 3 threads nommés `T-1`, `T-2`, `T-3` ;
  * chacun « simule » un I/O via `Thread.sleep(200)` puis logge son nom ;
  * mesure le **temps total**.
    **Question :** Pourquoi la durée totale est ~200–220 ms et pas 600 ms ?

### Exercice 2 — Executors / Pools

**But :** soumettre des tâches à un pool.

* Implémentez `ex2_executors()` avec `Executors.newFixedThreadPool(2)` ;
* soumettez 6 tâches `sleep(150)` ;
* comparez le **makespan** (durée totale) avec 2, puis 4 threads dans le pool.

### Exercice 3 — Callable + Future

**But :** retour de valeur + gestion d’exception.

* Implémentez `ex3_future()` : soumettre 5 `Callable<Integer>` retournant un score ;
* récupérez les résultats avec `Future#get()` ;
* gérez `ExecutionException`.

### Exercice 4 — CompletableFuture

**But :** composition asynchrone.

* Implémentez `ex4_completableFuture()` :

  * `supplyAsync` de deux tâches « A » et « B » (200 ms chacune) ;
  * combinez-les avec `thenCombine` ;
  * ajoutez `orTimeout` et `exceptionally` ;
  * logguez le **thread courant** pour chaque étape.
    **Question :** Que se passe-t-il si vous forcez un timeout à 100 ms ?

### Exercice 5 — Parallel Streams

**But :** paralléliser des opérations sur collections.

* Implémentez `ex5_parallelStreams()` : calculer la somme des carrés `1..N` ;
* variez `N` (1e5, 1e6) ;
* comparez `stream()` vs `parallelStream()` et discutez.

### Exercice 6 — Virtual Threads (Java 21)

**But :** découvrir la scalabilité I/O « cheap blocking ».

* Implémentez `ex6_virtualThreads()` avec `Executors.newVirtualThreadPerTaskExecutor()` ;
* créez **10 000 tâches** qui font `sleep(10)` puis retournent un identifiant ;
* vérifiez la **stabilité** (pas d’OutOfMemoryError) et la **durée** ;
* comparez avec un `FixedThreadPool(200)`.
  **Question :** Pourquoi le modèle tient la charge avec VT ?

### Exercice 7 — Structured Concurrency (Java 21)

**But :** fan-out/fan-in propre + propagation des échecs.

* Implémentez `ex7_structured()` :

  * `ShutdownOnFailure` ; forkez 3 appels : `fetchUser()`, `fetchOrders()`, `fetchNotifications()` ;
  * introduisez une exception dans l’une des tâches ;
  * observez `join()`, `throwIfFailed()` et l’annulation des autres.

