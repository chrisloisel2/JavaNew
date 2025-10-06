# Java 9 — Modularité et performances

Ce module illustre les nouveautés majeures de Java 9 :

- **Modules (Project Jigsaw)** : le fichier `module-info.java` déclare les exports afin de contrôler la visibilité. Importez le module avec `requires com.example.java9;` depuis un autre module.
- **API Stream enrichie** : la classe `StreamEnhancements` montre `takeWhile`, `dropWhile` et la nouvelle surcharge de `Stream.iterate` permettant de préciser un prédicat d'arrêt.
- **JShell** : lancez `jshell` puis importez `com.example.java9.streams.StreamEnhancements` pour expérimenter en REPL sans créer de classe.
- **G1 GC par défaut** : aucune action nécessaire pour l'utiliser, mais le README documente le changement pour sensibiliser les équipes aux performances.

```shell
# Exemple de session JShell
jshell> /open src/main/java/com/example/java9/module/ModuleGreeting.java
jshell> new ModuleGreeting().greet("Java 9")
$1 ==> "Bonjour, Java 9 !"
```
