# TP1 – Application console de bibliothèque modulaire

## Objectifs pédagogiques
- Combiner l'ensemble des notions abordées dans le cours.
- Concevoir une architecture multi-modules complète.
- Gérer la persistance, les flux et l'interaction utilisateur.

## Mise en situation
Vous livrez un MVP de gestion de bibliothèque pour un réseau de médiathèques. L'application doit fonctionner en mode console tout en restant facilement extensible.

## Exercices
1. **Architecture générale**  
   Définissez trois modules : `com.biblio.core`, `com.biblio.storage`, `com.biblio.cli`. Décrivez les dépendances via `module-info.java` et la structure de packages.

2. **Modèle métier**  
   Dans `com.biblio.core`, implémentez des `record` pour `Livre`, `Utilisateur`, `Emprunt`. Ajoutez une classe scellée `CatalogueEvent` (`LivreCree`, `LivreMisAJour`, `LivreSupprime`).

3. **Gestion des données**  
   Dans `com.biblio.storage`, fournissez des adaptateurs pour :
   - stockage fichier (NIO.2 + sérialisation JSON ou texte),
   - stockage mémoire (collections),
   - synchronisation distante simulée (`HttpClient`).
   Gérez les exceptions via une hiérarchie `StorageException`.

4. **Interface utilisateur**  
   Dans `com.biblio.cli`, créez un menu textuel (Text Blocks) permettant d'ajouter/rechercher des livres, d'enregistrer un emprunt et de générer un rapport via Streams (`groupingBy`).

5. **Tests fonctionnels**  
   Fournissez un script `main` qui enchaîne les cas d'usage principaux et valide le comportement (gestion des erreurs, relances utilisateur).

## Pistes d'extension
- Intégrer un module `com.biblio.analytics` exploitant les streams et `Optional`.
- Ajouter un mécanisme de plugins via le Service Loader.
