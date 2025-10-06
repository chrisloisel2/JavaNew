# Java 10 — Lisibilité et inférence

- **`var` pour les variables locales** : la classe `VarShowcase` démontre l'inférence de type, y compris dans les boucles et les lambdas.
- **Optimisations JVM** : Java 10 améliore le ramasse-miettes (G1, Parallel GC) et le partage des threads. Consultez la JEP 312 pour les détails ; aucune configuration n'est requise pour profiter des améliorations.

```java
var showcase = new VarShowcase();
System.out.println(showcase.formatMessage());
```
