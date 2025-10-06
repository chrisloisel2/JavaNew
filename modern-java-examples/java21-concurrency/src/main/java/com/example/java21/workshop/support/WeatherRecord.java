package com.example.java21.workshop.support;

import java.time.Instant;

/**
 * Représente une mesure météo brute fournie par un capteur.
 */
public record WeatherRecord(String city, double temperatureCelsius, double humidityPercentage, Instant timestamp) {
}
