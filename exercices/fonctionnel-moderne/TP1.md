# TP1 – Pipelines fonctionnels pour la data science

## Objectifs pédagogiques
- Manipuler les interfaces fonctionnelles standard.
- Construire un pipeline complet avec l'API Stream.
- Utiliser les `Collectors` pour produire des agrégats.

## Mise en situation
Une équipe data souhaite expérimenter une chaîne de transformation sur des mesures énergétiques. Vous devez fournir un prototype flexible.

## Exercices
1. **Génération de données**  
   Créez un `Supplier<Double>` qui génère des mesures aléatoires. Utilisez `Stream.generate` pour produire 100 valeurs et stockez-les dans une `List<Double>`.

2. **Filtrage et transformation**  
   Définissez un `Predicate<Double>` pour filtrer les valeurs aberrantes (en dehors d'un intervalle). Transformez les mesures en `BigDecimal` via une `Function<Double, BigDecimal>` en arrondissant à deux décimales.

3. **Agrégation**  
   Utilisez `Collectors.summarizingDouble` pour obtenir des statistiques. Créez également un `Collector` personnalisé qui regroupe les mesures par tranche (`0-10`, `10-20`, etc.).

4. **Visualisation textuelle**  
   Consommez le flux avec un `Consumer<Map<String, Long>>` qui produit un histogramme textuel grâce aux text blocks.

## Pistes d'extension
- Intégrer `Optional` pour gérer l'absence de données.
- Exporter le résultat dans un fichier via `Files.writeString`.
