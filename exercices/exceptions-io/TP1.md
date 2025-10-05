# TP1 – Gestion robuste des fichiers

## Objectifs pédagogiques
- Manipuler les API I/O classiques et NIO.2.
- Gérer les exceptions avec `try-catch` et `try-with-resources`.
- Organiser le code pour assurer la résilience des opérations disque.

## Mise en situation
Vous développez un outil de synchronisation de fichiers pour une petite équipe. La fiabilité prime sur la performance.

## Exercices
1. **Copie sécurisée**  
   Implémentez une classe `FileCopier` avec une méthode `copier(Path source, Path destination)` utilisant `Files.newBufferedReader` et `Files.newBufferedWriter` dans un bloc `try-with-resources`. Gérez les `IOException` avec journalisation.

2. **Vérification post-copie**  
   Ajoutez une méthode `boolean verifierIntegrite(Path source, Path destination)` qui compare la taille et le hash (via `MessageDigest`). Lancez une exception personnalisée `IntegrityException` si le contrôle échoue.

3. **Historique des opérations**  
   Conservez un journal des copies réussies/échouées dans une `List<String>`. Sérialisez le journal dans un fichier texte via `Files.write` à la fin du programme.

4. **WatchService de surveillance**  
   Créez un composant `DirectoryWatcher` qui surveille un répertoire source et déclenche automatiquement `copier` lors de la création d'un nouveau fichier.

## Pistes d'extension
- Ajouter une option de synchronisation bidirectionnelle.
- Gérer les fichiers binaires via des canaux (`FileChannel`).
