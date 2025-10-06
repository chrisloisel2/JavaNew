package com.example.patterns.structural.adapter;

/**
 * API existante qui expose une méthode incompatible.
 */
public class LegacyAnalytics {
    public String analyzeCsv(String csv) {
        return "Legacy analytics on " + csv;
    }
}
