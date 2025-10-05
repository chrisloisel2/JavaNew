# Exemples pratiques pour le cours Java 21

Ce projet Maven regroupe un ensemble de mini-scénarios qui illustrent chaque thème abordé dans le support de cours. Chaque package se concentre sur un concept et fournit :

- un contexte métier réaliste ;
- des classes et méthodes démontrant les bonnes pratiques ;
- un point d'entrée dans `DemoRunner` pour exécuter l'exemple depuis la ligne de commande.

## Structure

| Package | Thème du cours | Description rapide |
| --- | --- | --- |
| `com.cours.exemple.basics.variables` | Déclaration de variables | Gestion d'un profil client avec différents types et constantes |
| `com.cours.exemple.basics.controlflow` | Structures de contrôle | Simulation d'approbation de crédit |
| `com.cours.exemple.basics.loops` | Boucles | Rééquilibrage d'inventaire sur plusieurs entrepôts |
| `com.cours.exemple.basics.methods` | Méthodes et fonctions | Calculs de santé pour une application de remise en forme |
| `com.cours.exemple.basics.strings` | Présentation des chaînes | Génération de messages personnalisés |
| `com.cours.exemple.basics.operators` | Opérateurs | Calcul des remises et de la TVA |
| `com.cours.exemple.basics.comments` | Syntaxe des commentaires | Modèle documenté de configuration |
| `com.cours.exemple.basics.scope` | Portée des variables | Suivi de métriques avec différentes portées |
| `com.cours.exemple.oop.classes` | Introduction aux classes et objets | Gestionnaire de tâches avec instanciation |
| `com.cours.exemple.oop.encapsulation` | Encapsulation | Compte bancaire avec contrôles d'accès |
| `com.cours.exemple.oop.inheritance` | Héritage | Hiérarchie de transports |
| `com.cours.exemple.oop.polymorphism` | Polymorphisme, surcharge et redéfinition | Impression polymorphe de rapports |
| `com.cours.exemple.oop.interfaces` | Interfaces | Services de notification multi-canaux |
| `com.cours.exemple.principles.dry` | Principe DRY | Centralisation de modèles d'e-mails |
| `com.cours.exemple.principles.kiss` | Principe KISS | Calculatrice de budget épurée |
| `com.cours.exemple.principles.solid` | Exercice SOLID | Traitement d'une commande découplé |

## Exécution

```bash
mvn -q -pl . exec:java -Dexec.mainClass=com.cours.exemple.DemoRunner
```

Chaque démonstration peut également être invoquée directement via les méthodes statiques exposées dans `DemoRunner`.
