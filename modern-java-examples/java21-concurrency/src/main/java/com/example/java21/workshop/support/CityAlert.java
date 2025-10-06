package com.example.java21.workshop.support;

import java.time.Instant;

/**
 * Message envoyé lorsqu'un seuil de confort est dépassé.
 */
public record CityAlert(String city, double comfortIndex, Instant triggeredAt) {
    public String formatted() {
        return "Alerte sur %s (indice %.2f) à %s".formatted(city, comfortIndex, triggeredAt);
    }
}
