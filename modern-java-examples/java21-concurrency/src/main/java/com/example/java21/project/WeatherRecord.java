package com.example.java21.project;

import java.time.Instant;

/**
 * Représente une mesure météorologique réalisée par une sonde distante.
 *
 * <p>Les records introduits dans Java&nbsp;16 réduisent considérablement le code boilerplate
 * tout en offrant une immutabilité pratique pour la programmation concurrente.</p>
 */
public record WeatherRecord(String city, double temperatureCelsius, double humidityPercentage, Instant measuredAt) {
}
