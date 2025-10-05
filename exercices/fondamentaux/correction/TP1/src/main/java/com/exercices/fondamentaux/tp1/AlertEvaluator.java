package com.exercices.fondamentaux.tp1;

final class AlertEvaluator {

    private AlertEvaluator() {
    }

    static SensorStatus evaluateWithIfElse(double mesure, double seuil) {
        if (mesure < seuil * 0.85) {
            return SensorStatus.OK;
        } else if (mesure < seuil) {
            return SensorStatus.ATTENTION;
        }
        return SensorStatus.CRITIQUE;
    }

    static SensorStatus evaluateWithSwitch(double mesure, double seuil) {
        double ratio = mesure / seuil;
        return switch ((int) (ratio * 10)) {
            case Integer.MIN_VALUE -> SensorStatus.CRITIQUE; // Sécurité
            case 0, 1, 2, 3, 4, 5, 6, 7 -> SensorStatus.OK;
            case 8, 9 -> SensorStatus.ATTENTION;
            default -> SensorStatus.CRITIQUE;
        };
    }
}
