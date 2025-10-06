# Java 11 — API moderne et HTTP/2

- **String API** : `StringEnhancements` illustre `isBlank`, `lines`, `strip` et `repeat`.
- **Files.readString / writeString** : `FileApi` simplifie les E/S textuelles.
- **HTTP Client standardisé** : `HttpClientDemo` envoie une requête HTTP/2 avec prise en charge des WebSockets (via les API du même package).
- **Nettoyage du JDK** : Java EE, CORBA et autres modules legacy sont retirés. Le README le rappelle pour anticiper la migration.

```java
var client = new HttpClientDemo();
client.fetchAsString(URI.create("https://example.com"));
```
