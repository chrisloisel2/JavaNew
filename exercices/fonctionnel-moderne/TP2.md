# TP2 – Java moderne et architecture modulaire

## Objectifs pédagogiques
- Utiliser les modules Java (Project Jigsaw).
- Mettre en œuvre records, pattern matching et `switch` expressions.
- Employer `HttpClient` et `CompletableFuture` pour des traitements asynchrones.

## Mise en situation
Vous développez un service de recommandations de livres pour une application mobile. Il doit agréger des données locales et distantes.

## Exercices
1. **Structure modulaire**  
   Créez deux modules `com.app.catalog` et `com.app.reco`. Définissez les `module-info.java` nécessaires pour exporter les packages utiles.

2. **Modèle de données moderne**  
   Dans `com.app.catalog`, modélisez un `sealed interface Media permits Book, AudioBook`. `Book` et `AudioBook` sont des `record` avec des attributs pertinents.

3. **Requêtes asynchrones**  
   Dans `com.app.reco`, implémentez un service qui interroge une API distante simulée via `HttpClient` et `CompletableFuture`. Combinez les résultats avec le catalogue local.

4. **Présentation intelligente**  
   Créez une méthode `String decrire(Media media)` utilisant `switch` avec pattern matching pour différencier les types et générer un résumé localisé.

## Pistes d'extension
- Ajouter des tests modulaires avec `java --module-path`.
- Intégrer un système de mise en cache avec `ConcurrentHashMap`.
