# TP1 – Gestionnaire de tâches collaboratif

## Objectifs pédagogiques
- Utiliser `List`, `Set` et `Map` pour organiser des données.
- Maîtriser les génériques et éviter les conversions de type.
- Mettre en œuvre des algorithmes simples de tri et filtrage.

## Mise en situation
Vous développez un prototype de gestionnaire de tâches pour une équipe agile. Le système doit suivre les tâches, l'affectation et l'état d'avancement.

## Exercices
1. **Modèle de données**  
   Créez un `record Task(String id, String titre, Status status, Set<String> labels)`. Définissez l'énumération `Status` (`TODO`, `IN_PROGRESS`, `DONE`).

2. **Gestion via `List`**  
   Implémentez une classe `TaskBoard` avec une `List<Task>` interne. Ajoutez des méthodes pour ajouter, supprimer par identifiant et rechercher par libellé. Assurez-vous que les opérations sont génériques et ne nécessitent pas de cast.

3. **Indexation rapide**  
   Ajoutez une `Map<String, Task>` pour l'accès direct par identifiant. Synchronisez la `Map` avec la `List` lors des opérations d'ajout/suppression.

4. **Affichage trié**  
   Fournissez une méthode `afficherParEtat()` qui retourne une `Map<Status, List<Task>>` triée par ordre de progression. Utilisez `Comparator` et `Collections.sort` ou `List.sort`.

## Pistes d'extension
- Introduire un `LinkedHashSet` pour conserver l'ordre d'insertion des libellés.
- Ajouter un export CSV des tâches terminées.
