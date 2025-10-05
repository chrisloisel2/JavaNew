# TP2 – Analyse de données financières

## Objectifs pédagogiques
- Exploiter les streams sur les collections.
- Utiliser `Optional` pour gérer l'absence de valeur.
- Mettre en place des structures adaptées aux recherches avancées.

## Mise en situation
Un cabinet de conseil doit analyser des transactions financières pour identifier des tendances et des anomalies. Vous devez préparer un module d'analyse.

## Exercices
1. **Structure de transactions**  
   Définissez un `record Transaction(String id, String categorie, double montant, LocalDate date)`. Préparez une `List<Transaction>` de données d'exemple dans une classe `TransactionSamples`.

2. **Statistiques par catégorie**  
   Implémentez une méthode `Map<String, Double> totalParCategorie(List<Transaction>)` utilisant `Collectors.groupingBy` et `Collectors.summingDouble`.

3. **Recherche conditionnelle**  
   Créez une méthode `Optional<Transaction> rechercherPlusGrande(String categorie)` qui retourne la transaction la plus élevée pour une catégorie donnée via `max` et `Comparator`.

4. **Détection d'anomalies**  
   Filtrez les transactions supérieures à une moyenne glissante sur 7 jours. Stockez les alertes dans une `Deque<Transaction>` pour un traitement FIFO. Documentez le choix des structures dans les commentaires.

## Pistes d'extension
- Exporter un rapport formaté en JSON ou CSV.
- Ajouter une interface pour brancher différents algorithmes de détection.
