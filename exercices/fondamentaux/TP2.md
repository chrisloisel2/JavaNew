# TP2 – Conception de services utilitaires

## Objectifs pédagogiques
- Concevoir des méthodes réutilisables.
- Appliquer les principes KISS et séparation des responsabilités.
- Écrire des tests de vérification simples via `main`.

## Mise en situation
Une petite start-up souhaite publier une bibliothèque utilitaire pour le traitement de données textuelles. Vous devez préparer un premier jet propre et documenté.

## Exercices
1. **Nettoyage et normalisation**  
   Créez une classe `TextCleaner` avec des méthodes `trim`, `toTitleCase`, `removeAccents`. Chaque méthode doit être `static` et documentée via JavaDoc.

2. **Analyse statistique**  
   Ajoutez une classe `TextAnalyzer` avec une méthode `countWords(String)` qui renvoie une `Map<String, Integer>`. Introduisez une surcharge acceptant une liste de mots à ignorer.

3. **Point d'entrée de démonstration**  
   Écrivez une classe `DemoApp` avec une méthode `main` qui illustre l'utilisation des services. Utilisez des boucles pour présenter les résultats et comparez deux implémentations (brute vs refactorisée) pour mettre en avant la lisibilité.

4. **Validation et tests rapides**  
   Ajoutez des assertions (`Objects.requireNonNull`, `assert`) pour garantir la validité des entrées. Fournissez un scénario de test dans `main` qui couvre les cas limites.

## Pistes d'extension
- Transformer `TextAnalyzer` en classe immuable recevant la configuration via un constructeur.
- Ajouter un mode interactif (lecture console) pour tester les méthodes.
