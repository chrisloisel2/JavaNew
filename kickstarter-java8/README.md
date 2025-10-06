# Dataset Kickstarter (Java 8)

Cette variante du module `kickstarter` fournit les mêmes données d'exemple mais avec des classes Java 8 "classiques"
(plutôt que des `record`). Les collections exposées sont immuables et prêtes à l'emploi pour les exercices sur les streams.

## Contenu
- Package `com.example.kickstarter.simple` contenant des POJO (`Project`, `Backer`, `Reward`, `Pledge`, `Category`).
- Classe utilitaire `KickstarterData` offrant des listes et des flux sur les données.
- API volontairement minimaliste : constructeurs, getters, `equals/hashCode` et quelques méthodes métiers (`fundingProgress`,
  `isFinished`).

## Utilisation
1. Ajoutez le dossier `kickstarter-java8/src/main/java` à votre projet ou IDE.
2. Importez le package `com.example.kickstarter.simple`.
3. Travaillez avec les collections immuables fournies ou utilisez les méthodes `streamProjects`, `streamBackers` et
   `streamPledges`.

> 💡 Cette version est compatible avec Java 8 et facilite l'intégration dans des projets ne supportant pas encore les `record`.
