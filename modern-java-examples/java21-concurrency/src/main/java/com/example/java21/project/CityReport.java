package com.example.java21.project;

/**
 * Résultat agrégé de plusieurs tâches asynchrones.
 */
public record CityReport(String city, double comfortIndex, WeatherRecord weather, int trafficDelayMinutes) {
}
