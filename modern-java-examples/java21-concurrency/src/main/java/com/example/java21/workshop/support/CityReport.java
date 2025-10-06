package com.example.java21.workshop.support;

/**
 * Agrégation de différentes mesures pour produire un rapport par ville.
 */
public record CityReport(String city, double comfortIndex, WeatherRecord weather, int trafficDelayMinutes) {
}
