# TP1 – Bases du langage : variables et structures de contrôle

## Objectifs pédagogiques
- Manipuler les types primitifs et les chaînes.
- Utiliser les structures conditionnelles et les boucles.
- Structurer un petit programme console selon les bonnes pratiques.

## Mise en situation
Vous devez réaliser un module de suivi de capteurs pour un laboratoire de météo amateur. Les données sont encore simulées, mais le code doit être propre et facilement maintenable.

## Exercices
1. **Initialisation des capteurs**  
   Déclarez toutes les catégories de types primitifs nécessaires pour représenter un capteur (identifiant, emplacement, mesure, seuil critique, actif/inactif). Affichez un récapitulatif formaté en utilisant la concaténation ou `String.format`.

2. **Logique d'alerte**  
   Implémentez une méthode qui prend une mesure simulée et affiche un message d'état différent selon des seuils : "OK", "Attention" ou "Critique". Comparez une version utilisant `if/else` et une version avec `switch` expression.

3. **Boucles de collecte**  
   Simulez la collecte de 24 mesures horaires avec une boucle `for`. Calculez la moyenne journalière, la valeur maximale et indiquez si un dépassement de seuil est survenu. Isolez les calculs dans des méthodes dédiées pour respecter le principe DRY.

4. **Reporting journalier**  
   Combinez les résultats précédents dans une méthode `genererRapportJournalier()` qui retourne un bloc de texte (text block) présentant les statistiques. Ajoutez une option permettant de ré-exécuter la simulation tant que l'utilisateur répond `o` via une boucle `do/while`.

## Pistes d'extension
- Lire les paramètres de seuil depuis les arguments de ligne de commande.
- Ajouter une validation pour empêcher les valeurs négatives lors de la saisie utilisateur.
