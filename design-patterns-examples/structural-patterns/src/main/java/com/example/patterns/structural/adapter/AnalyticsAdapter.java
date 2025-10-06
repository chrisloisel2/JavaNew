package com.example.patterns.structural.adapter;

/**
 * Adapter convertit un flux JSON en CSV pour la bibliothèque historique.
 */
public class AnalyticsAdapter {

    private final LegacyAnalytics legacyAnalytics;

    public AnalyticsAdapter(LegacyAnalytics legacyAnalytics) {
        this.legacyAnalytics = legacyAnalytics;
    }

    public String analyzeJson(String json) {
        String csv = json.replace('{', ' ').replace('}', ' ').replace(':', ';');
        return legacyAnalytics.analyzeCsv(csv);
    }
}
