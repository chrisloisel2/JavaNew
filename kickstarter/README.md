# Données Kickstarter pour exercices Streams

Ce module fournit un jeu de données Java prêt à l'emploi pour expérimenter l'API Stream.

## Contenu
- `Category` : enum des catégories disponibles.
- `Reward` : record décrivant un palier de récompense.
- `Project` : record décrivant une campagne Kickstarter et exposant des aides (`fundingProgress`, `isFinished`).
- `Backer` : record représentant un contributeur.
- `Pledge` : record représentant une promesse de contribution.
- `KickstarterData` : agrégateur contenant les collections immuables (`PROJECTS`, `BACKERS`, `PLEDGES`) et plusieurs méthodes utilitaires.

## Utilisation rapide
```java
var projects = KickstarterData.PROJECTS;
var fundedDesignProjects = KickstarterData.streamProjects()
        .filter(project -> project.category() == Category.DESIGN)
        .filter(project -> project.fundingProgress() >= 100)
        .toList();
```

Les collections exposées sont immuables ; si vous avez besoin de structures modifiables, créez une copie :

```java
var mutableCopy = new ArrayList<>(KickstarterData.PROJECTS);
```

Bon prototypage !
