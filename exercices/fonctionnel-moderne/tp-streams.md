# TP : Explorer l'API Stream de Java avec Kickstarter

## Objectifs pédagogiques
- Manipuler des flux (`Stream`) sur des collections riches.
- Définir et combiner des `Predicate`, `Function`, `Consumer`, `Supplier` et `Comparator`.
- Utiliser les opérations intermédiaires et terminales des streams (`map`, `filter`, `sorted`, `distinct`, `flatMap`, `peek`, etc.).
- Mettre en œuvre les collecteurs (`Collectors`) pour agréger, partitionner et grouper des données.
- Concevoir un mini-pipeline de traitement fonctionnel lisible, testable et réutilisable.

## Mise en place
1. Ouvrez le dossier [`kickstarter`](../../kickstarter) qui contient un jeu de données orienté « crowdfunding ».
2. Si vous travaillez en Java 8, utilisez la variante [`kickstarter-java8`](../../kickstarter-java8) et son package `com.example.kickstarter.simple`.
3. Ajoutez le package approprié à votre projet (Maven/Gradle/IDEA/Eclipse). Les classes sont prêtes à l'emploi.
4. Créez une classe de tests (JUnit) ou une classe `main` dédiée (`KickstarterStreamPlayground`) pour réaliser les exercices.
5. Dans vos solutions, privilégiez les expressions lambda plutôt que les classes anonymes, sauf mention contraire.
6. Adaptez les références de méthodes (`Project::title` vs `Project::getTitle`) selon la version des classes que vous utilisez.

## Rappel du modèle de données
- `Project` : campagne Kickstarter (catégorie, pays, dates de lancement/fin, montants, tags, récompenses...).
- `Backer` : personne qui soutient des projets (pays, intérêts, budget annuel, statut pro).
- `Reward` : palier de récompense (titre, minimum, stock limité, livraison estimée).
- `Pledge` : contribution d'un backer à un projet (montant, date, récompense choisie).
- `KickstarterData` : fournit des collections immuables pré-remplies et des méthodes utilitaires pour créer des flux.

> 💡 Lisez le JavaDoc de chaque classe et explorez les données fournies pour mieux comprendre les attributs disponibles.

## Travaux pratiques

### 1. Premiers pas avec les flux
- Écrire un stream qui renvoie les titres des 5 projets qui se terminent le plus rapidement.
- Utiliser un `Comparator` composé (par `Comparator.comparing`) puis `map(Project::title)`.
- Ajouter un `peek` pour logger l'identifiant des projets (via un `Consumer<Project>` dédié).
- Variante : afficher également le pays formaté via `KickstarterData.countryDisplayName` en utilisant `map` puis `forEach`.
- Extra : ré-écrire l'exercice avec `Stream.iterate` pour simuler un flux infini puis `limit`.

### 2. Prédicats réutilisables
- Créer une classe `ProjectPredicates` contenant des méthodes retournant des `Predicate<Project>` (ex. : `isFunded()`, `isTrending()`, `belongsToCategory(String)`).
- Mettre en pratique : filtrer les projets financés à plus de 110 % dans la catégorie *Design*.
- Écrire un test paramétré qui combine les prédicats avec `and` / `or`.
- Bonus : proposer un prédicat `launchedBetween(LocalDate, LocalDate)` réutilisable dans plusieurs tests.
- Challenge : écrire un prédicat configurable via builder (ex. `new ProjectFilter().withMinFunding(150).locatedIn("US")`).

### 3. Fonctions de transformation
- Construire une `Function<Project, Optional<Reward>>` qui renvoie la récompense la plus accessible (`min` sur `minimumPledge`).
- À partir de cette fonction, produire la liste des titres des récompenses d'entrée de gamme.
- Bonus : utilisez `flatMap(Optional::stream)` pour éliminer les projets sans récompense.
- Variante : exposer la fonction sous forme de `UnaryOperator<Project>` qui ajuste les montants (`map` sur une copie immuable).
- Challenge : écrire une `Function<Project, Map<String, Object>>` pour sérialiser un projet en représentation JSON-friendly.

### 4. Consumers et Supplier
- Écrire un `Consumer<Pledge>` qui affiche un reçu formaté (utiliser `String.format`).
- Créer un `Supplier<Stream<Pledge>>` basé sur `KickstarterData.streamPledges()` permettant de ré-exécuter plusieurs traitements indépendants sans dupliquer le code d'accès aux données.
- Mettre en place deux traitements :
  1. Montant total investi par pays des backers.
  2. Classement des trois projets les plus soutenus par des backers professionnels.
- Extra : brancher un `Consumer<Project>` qui alimente un logger ou un buffer mémoire pour générer un rapport.
- Challenge : mettre en place un `Supplier<Double>` qui calcule paresseusement le budget moyen des backers par pays.

### 5. Collectors avancés
- Grouper les projets par catégorie (`Collectors.groupingBy`) et calculer :
  - le nombre de projets,
  - le taux de financement moyen (pledged/goal).
- Partitionner les backers selon leur statut (`Collectors.partitioningBy(Backer::isProfessional)`).
- Créer une carte triée (`TreeMap`) qui classe les projets par mois de lancement (`YearMonth`) et par montant total collecté (descending).
- Extra : créer un `Collector` personnalisé qui construit une `String` multi-ligne décrivant chaque catégorie.
- Challenge : implémenter un `Collector` multi-niveaux qui groupe par pays puis par catégorie avec des `LinkedHashMap` pour conserver l'ordre d'insertion.

### 6. Pipelines complets
- Construire un pipeline qui enchaîne :
  1. Sélection des projets financés (`Predicate`).
  2. Transformation en résumé (`Function<Project, ProjectSummary>` que vous définissez).
  3. Tri par pourcentage de financement.
  4. Collecte dans une `LinkedHashMap<String, Double>` (titre → pourcentage) en conservant l'ordre.
- Implémenter `ProjectSummary` comme un `record`.
- Prévoir un test qui valide à minima le top 3 attendu (précision ±0,01).
- Variante : créer un pipeline équivalent qui retourne un `DoubleStream` et utilise `summaryStatistics`.
- Challenge : exposer ce pipeline comme une `Function<Stream<Project>, Stream<ProjectSummary>>` et chaîner plusieurs fonctions via `andThen`.

### 7. Réduction, statistiques et optionnel
- Calculer la médiane des montants de promesse (`Pledge.amount()`). (Astuce : trier puis utiliser `skip`/`limit`).
- Utiliser `Collectors.summarizingDouble` pour produire des statistiques descriptives par catégorie.
- Écrire une méthode qui renvoie le `Optional<Project>` correspondant au projet ayant reçu une promesse un 1er janvier.
- Extra : créer un calcul de variance et d'écart-type à l'aide d'une réduction personnalisée.
- Challenge : implémenter une recherche dichotomique sur un `DoubleStream` obtenu via `mapToDouble` pour trouver un seuil de rentabilité.

### 8. Bonus créatifs
- Implémenter une pipeline réutilisable (`Function<Stream<Project>, Stream<Project>>`) qui applique un ensemble de filtres dynamiques.
- Utiliser un `Predicate` construit à partir d'un fichier de configuration (parsing d'un `Properties`).
- Expérimenter les `Collector` personnalisés : écrivez un collector qui construit un histogramme (Map plage de montants → nombre de promesses).
- Extra : relier vos streams à une API tierce (CSV, JSON) pour exporter un rapport final.
- Challenge : écrire un `Spliterator` personnalisé pour itérer sur les `Pledge` par fenêtre glissante.

### 9. Exercices sur les parallélisations contrôlées
- Comparer un traitement séquentiel et un traitement parallèle (`parallelStream`) sur le calcul du montant total investi.
- Mettre en place un `ForkJoinPool` personnalisé pour maîtriser le niveau de parallélisme.
- Explorer les problématiques de thread-safety : collectez les résultats dans une structure mutable non synchronisée puis dans une structure thread-safe.

### 10. Tests et validation
- Écrire des tests de non-régression sur vos pipelines via `@MethodSource` (JUnit 5).
- Ajouter un test de performance (JMH ou micro-bench maison) pour comparer deux implémentations d'un même calcul.
- Mettre en place des assertions sur les `Optional` (`assertTrue(optional.isPresent())`) et sur les statistiques calculées.

### 11. Exercices full FP
- Recréez une version `Project` immutable manuellement (constructeur privé + builder) et adaptez vos streams en conséquence.
- Utilisez `Stream#reduce` pour implémenter un mini moteur de règles fonctionnelles appliqué aux `Backer`.
- Composez plusieurs `Function` et `Predicate` via `compose`/`andThen` pour créer un pipeline déclaratif unique.

### 12. Défis de synthèse
- Réalisez une commande `TopRewards` qui combine `Project`, `Reward` et `Pledge` pour afficher les récompenses les plus populaires.
- Créez un tableau de bord texte avec `StringBuilder` + `forEachOrdered` résumant : projets financés, montants moyens, top backers.
- Imaginez un traitement complet : ingestion (`Supplier`), filtrage (`Predicate`), transformation (`Function`), agrégation (`Collector`) et rendu (`Consumer`). Documentez chaque étape.

## Livrables attendus
- Le code source de vos solutions (tests ou classe `main`).
- Un court rapport Markdown décrivant les choix réalisés, les points de blocage et les pistes d'amélioration.
- (Bonus) Un graphique ou tableau synthétique produit via un export CSV/JSON et traité avec un outil externe.

## Conseils
- Commencez simple : utilisez `Stream` directement, puis factorisez vos prédicats/fonctions.
- Pensez à la lisibilité : nommez vos lambdas ou extraire des méthodes lorsque le pipeline devient long.
- Utilisez les tests unitaires pour figer des comportements attendus et éviter les régressions.
- Documentez les cas limites (collections vides, montants nulls) et le comportement choisi.

Bon TP ! 🚀
