package com.example.java21.project;

import java.time.Instant;

/**
 * Évènement envoyé via un flux réactif lorsque l'indice de confort dépasse un seuil.
 */
public record CityAlert(String city, double comfortIndex, Instant emittedAt, String source) {
}
