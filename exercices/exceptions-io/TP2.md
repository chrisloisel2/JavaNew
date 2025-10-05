# TP2 – Sérialisation et persistance avancée

## Objectifs pédagogiques
- Mettre en œuvre la sérialisation binaire et JSON.
- Gérer plusieurs niveaux d'exceptions personnalisées.
- Concevoir un module de persistance modulaire.

## Mise en situation
Une application de gestion de bibliothèque souhaite sauvegarder son catalogue localement et sur un service distant. Vous devez créer un module de persistance évolutif.

## Exercices
1. **Modèles sérialisables**  
   Créez un `record Livre(String isbn, String titre, String auteur, LocalDate parution)` qui implémente `Serializable`. Ajoutez un `record Emprunt(Livre livre, LocalDate debut, LocalDate fin)`.

2. **Sauvegarde binaire**  
   Implémentez une classe `BinaryStorage` avec `sauvegarder(List<Emprunt>)` et `charger()` utilisant `ObjectOutputStream`/`ObjectInputStream`. Gérez les `IOException` et `ClassNotFoundException`.

3. **Sauvegarde JSON**  
   Créez une interface `CatalogStorage` et une implémentation `JsonStorage` (pseudo-code si aucune librairie externe). Utilisez `java.net.http.HttpClient` pour envoyer le JSON vers un service distant simulé (console ou stub).

4. **Gestion des erreurs**  
   Définissez une hiérarchie d'exceptions (`StorageException`, `RemoteStorageException`). Ajoutez une stratégie de retry avec journalisation lorsque l'envoi distant échoue.

## Pistes d'extension
- Implémenter un mécanisme de versionning du schéma.
- Ajouter un mode de chiffrement optionnel lors de la sauvegarde.
