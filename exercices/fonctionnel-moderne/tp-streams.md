# TP : Explorer l'API Stream de Java avec Kickstarter

## Objectifs pédagogiques
- Manipuler des flux (`Stream`) sur des collections riches.
- Définir et combiner des `Predicate`, `Function`, `Consumer`, `Supplier` et `Comparator`.
- Utiliser les opérations intermédiaires et terminales des streams (`map`, `filter`, `sorted`, `distinct`, `flatMap`, `peek`, etc.).
- Mettre en œuvre les collecteurs (`Collectors`) pour agréger, partitionner et grouper des données.
- Concevoir un mini-pipeline de traitement fonctionnel lisible, testable et réutilisable.

## Mise en place
1. Ouvrez le dossier [`kickstarter`](../../kickstarter) qui contient un jeu de données orienté « crowdfunding ».
2. Ajoutez le package `com.example.kickstarter` à votre projet (Maven/Gradle/IDEA/Eclipse). Les classes sont prêtes à l'emploi.
3. Créez une classe de tests (JUnit) ou une classe `main` dédiée (`KickstarterStreamPlayground`) pour réaliser les exercices.
4. Dans vos solutions, privilégiez les expressions lambda plutôt que les classes anonymes, sauf mention contraire.

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

### 2. Prédicats réutilisables
- Créer une classe `ProjectPredicates` contenant des méthodes retournant des `Predicate<Project>` (ex. : `isFunded()`, `isTrending()`, `belongsToCategory(String)`).
- Mettre en pratique : filtrer les projets financés à plus de 110 % dans la catégorie *Design*.
- Écrire un test paramétré qui combine les prédicats avec `and` / `or`.

### 3. Fonctions de transformation
- Construire une `Function<Project, Optional<Reward>>` qui renvoie la récompense la plus accessible (`min` sur `minimumPledge`).
- À partir de cette fonction, produire la liste des titres des récompenses d'entrée de gamme.
- Bonus : utilisez `flatMap(Optional::stream)` pour éliminer les projets sans récompense.

### 4. Consumers et Supplier
- Écrire un `Consumer<Pledge>` qui affiche un reçu formaté (utiliser `String.format`).
- Créer un `Supplier<Stream<Pledge>>` basé sur `KickstarterData.streamPledges()` permettant de ré-exécuter plusieurs traitements indépendants sans dupliquer le code d'accès aux données.
- Mettre en place deux traitements :
  1. Montant total investi par pays des backers.
  2. Classement des trois projets les plus soutenus par des backers professionnels.

### 5. Collectors avancés
- Grouper les projets par catégorie (`Collectors.groupingBy`) et calculer :
  - le nombre de projets,
  - le taux de financement moyen (pledged/goal).
- Partitionner les backers selon leur statut (`Collectors.partitioningBy(Backer::isProfessional)`).
- Créer une carte triée (`TreeMap`) qui classe les projets par mois de lancement (`YearMonth`) et par montant total collecté (descending).

### 6. Pipelines complets
- Construire un pipeline qui enchaîne :
  1. Sélection des projets financés (`Predicate`).
  2. Transformation en résumé (`Function<Project, ProjectSummary>` que vous définissez).
  3. Tri par pourcentage de financement.
  4. Collecte dans une `LinkedHashMap<String, Double>` (titre → pourcentage) en conservant l'ordre.
- Implémenter `ProjectSummary` comme un `record`.
- Prévoir un test qui valide à minima le top 3 attendu (précision ±0,01).

### 7. Réduction, statistiques et optionnel
- Calculer la médiane des montants de promesse (`Pledge.amount()`). (Astuce : trier puis utiliser `skip`/`limit`).
- Utiliser `Collectors.summarizingDouble` pour produire des statistiques descriptives par catégorie.
- Écrire une méthode qui renvoie le `Optional<Project>` correspondant au projet ayant reçu une promesse un 1er janvier.

### 8. Bonus créatifs
- Implémenter une pipeline réutilisable (`Function<Stream<Project>, Stream<Project>>`) qui applique un ensemble de filtres dynamiques.
- Utiliser un `Predicate` construit à partir d'un fichier de configuration (parsing d'un `Properties`).
- Expérimenter les `Collector` personnalisés : écrivez un collector qui construit un histogramme (Map plage de montants → nombre de promesses).

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
