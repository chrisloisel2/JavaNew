# TP1 – Modélisation objet d'une animalerie

## Objectifs pédagogiques
- Manipuler classes, constructeurs et modificateurs d'accès.
- Comprendre l'encapsulation et la visibilité des membres.
- Illustrer l'héritage simple et la composition.

## Mise en situation
Vous devez concevoir le logiciel de gestion d'une petite animalerie. Le système doit être extensible car de nouveaux animaux seront ajoutés régulièrement.

## Exercices
1. **Classe de base `Animal`**  
   Créez une classe abstraite `Animal` avec des attributs privés (`nom`, `age`, `poids`) et des getters protégés. Ajoutez une méthode abstraite `makeSound()` et une méthode finale `vieillir()` qui incrémente l'âge.

2. **Implémentations spécialisées**  
   Implémentez `Chien` et `Chat` en redéfinissant `makeSound()`. Ajoutez des comportements spécifiques (`rapporterObjet`, `faireSesGriffes`). Documentez via JavaDoc pourquoi certains attributs restent privés.

3. **Gestionnaire d'enclos**  
   Créez une classe `Enclos` contenant une `List<Animal>`. Fournissez des méthodes pour ajouter, retirer et nourrir les animaux. Utilisez l'encapsulation pour éviter que la liste ne soit modifiable de l'extérieur.

4. **Simulation de visite**  
   Écrivez une classe `AnimalerieApp` avec `main` qui instancie plusieurs animaux, les place dans des enclos et invoque leurs comportements polymorphes. Ajoutez un `switch` expression pour formater la description selon le type concret.

## Pistes d'extension
- Introduire une interface `Soignable` avec une méthode `soigner()` à implémenter.
- Ajouter une classe `Oiseau` avec un comportement de vol et un suivi de la cage.
