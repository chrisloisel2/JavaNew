# TP : Système de traitement parallèle avec Threads et Processus

## Contexte

Votre entreprise développe un moteur d’analyse capable de :

* Exécuter des traitements en **parallèle localement** (threads).
* Déléguer certains calculs lourds à des **processus externes** (scripts Python, outils CLI, etc.).
* Surveiller et **journaliser l’état d’exécution** de chaque unité (thread ou process).

---

##  Gestion de threads concurrents

### Objectif :

Implémenter un **pool de threads** capable d’exécuter plusieurs tâches de calcul en parallèle.

### Étapes :

1. Créez une classe `ComputationTask` qui implémente `Callable<Double>`.

   * Elle doit simuler un calcul lourd (par exemple, somme d’un grand tableau aléatoire ou une intégrale numérique).
   * Chaque tâche doit afficher son **nom de thread** et le **temps d’exécution**.

2. Créez un `ExecutorService` fixe de 4 threads.

3. Lancez 10 tâches concurrentes et récupérez leurs résultats avec `Future<Double>`.

4. Mesurez le temps total d’exécution et comparez avec une exécution séquentielle.

💡 *Extension possible* : réécrire cette partie avec `CompletableFuture.supplyAsync()` pour profiter de la programmation fonctionnelle.

---

## Gestion de processus externes

### Objectif :

Lancer plusieurs **processus système** en parallèle (ex. scripts Python, commandes Linux/Windows) et superviser leur exécution.

### Étapes :

1. Créez une classe `ExternalJob` qui lance une commande (par ex. `ping`, `curl`, `python script.py`, ou `sleep`).
2. Utilisez l’API `ProcessBuilder` pour démarrer le processus et rediriger la sortie.
3. Récupérez un `ProcessHandle` pour :

   * Afficher le PID,
   * Le nom de l’utilisateur,
   * Le temps CPU consommé (`totalCpuDuration()`),
   * La date de démarrage (`startInstant()`).
4. Attendez la fin du processus de manière **non bloquante** avec `onExit()` (retourne un `CompletableFuture<Process>`).
5. Journalisez l’état et le code de sortie à la fin.

💡 *Extension* : ajouter une option pour tuer automatiquement les processus dépassant une durée limite (timeout).

---

## Coordination Threads ↔ Processus

### Objectif :

Créer une **orchestration combinée** :

* Des threads gèrent la logique métier (analyse des données),
* Des processus exécutent des scripts externes pour enrichir les résultats.

### Scénario proposé :

1. Chaque thread du pool lit un “fichier d’entrée” (ou un jeu de données aléatoire).
2. Une fois les données traitées, le thread déclenche un **processus externe** qui fait un post-traitement (ex. `python analyse.py <résultat>`).
3. Utilisez `CompletableFuture.allOf()` pour attendre que tous les threads + processus soient terminés.
4. Agrégez les sorties dans un fichier `final_report.txt`.

💡 Astuce : combinez `thenCompose()` pour enchaîner *thread → process → agrégation* sans bloquer.

---

##  Supervision et Monitoring

### Objectif :

Mettre en place un **suivi en temps réel** des threads et processus actifs.

### À faire :

1. Lancer un thread de monitoring qui affiche toutes les 2 secondes :

   * Le nombre de threads actifs (`Thread.activeCount()`),
   * Les PID des processus enfants (`ProcessHandle.current().children()`),
   * L’état (`isAlive()`) et la durée CPU cumulée de chaque processus.
2. Lorsque tous les calculs sont terminés, afficher :

   * Le temps total d’exécution global,
   * Le nombre total de threads et processus utilisés.

---

##  Bonus

1. **Exporter un graphe d’exécution** (threads ↔ processus) en JSON pour visualiser les dépendances.
   Exemple de format :

   ```json
   {
     "threads": ["T1", "T2", "T3"],
     "processes": ["P1234", "P1235"],
     "links": [["T1", "P1234"], ["T2", "P1235"]]
   }
   ```

2. Implémentez un **gestionnaire d’erreurs** global :

   * Si un thread échoue → relancer le calcul dans un autre thread.
   * Si un processus échoue → réexécuter jusqu’à 2 fois avant abandon.

3. Ajoutez une **interface console simple** avec `Scanner` :

   * `status` → affiche les threads et processus actifs
   * `kill <pid>` → tue un processus spécifique
   * `exit` → termine proprement tout le système
