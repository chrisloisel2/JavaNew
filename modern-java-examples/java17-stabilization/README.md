# Java 17 — Stabilisation

- **Records et sealed classes finalisés** : `BillingRecord` et `PaymentStatus` montrent la maturité des features.
- **Switch expressions** : la classe `SwitchPatterns` combine switch + sealed classes pour une exhaustivité contrôlée.
- **NullPointerException enrichis** : exécutez `new NullPointerDemo().trigger();` pour voir le message détaillé.
- **GC ZGC/Shenandoah intégrés** : disponibles via `-XX:+UseZGC` ou `-XX:+UseShenandoahGC` selon les besoins.
