# Java 12–16 — Expressivité et concision

- **Switch expressions** : `DayRateCalculator` utilise la nouvelle syntaxe avec flèches et `yield` implicite.
- **Text blocks** : `TextBlockExamples` maintient du JSON lisible dans le code.
- **Pattern matching pour `instanceof`** : `PatternMatchingDemo` évite les casts explicites.
- **Records** : `Point` génère automatiquement constructeur, accesseurs et `equals/hashCode`.
- **Sealed Classes** : l'interface `Shape` montre la hiérarchie fermée, apparue en preview dans cette période.

```java
Shape shape = new Shape.Rectangle(2, 5);
switch (shape) {
    case Shape.Circle c -> System.out.println(c.area());
    case Shape.Rectangle r -> System.out.println(r.area());
}
```
