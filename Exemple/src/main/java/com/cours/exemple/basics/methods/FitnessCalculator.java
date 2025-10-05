package com.cours.exemple.basics.methods;

/**
 * Met en évidence la définition et l'appel de méthodes avec paramètres et valeurs de retour.
 */
public final class FitnessCalculator {

    private FitnessCalculator() {
    }

    public static void example() {
        System.out.println("[Méthodes] Calculs pour une application de remise en forme");

        int age = 34;
        double weightKg = 68.5;
        double heightCm = 172;

        double bmi = bodyMassIndex(weightKg, heightCm);
        int maxHeartRate = maxHeartRate(age);

        System.out.printf("IMC: %.1f - Fréquence cardiaque max: %d bpm\n", bmi, maxHeartRate);
        System.out.printf("Zone d'effort modérée: %d-%d bpm\n", trainingZone(maxHeartRate, 0.5), trainingZone(maxHeartRate, 0.7));
    }

    private static double bodyMassIndex(double weightKg, double heightCm) {
        double heightMeters = heightCm / 100;
        return weightKg / (heightMeters * heightMeters);
    }

    private static int maxHeartRate(int age) {
        return 220 - age;
    }

    private static int trainingZone(int maxHeartRate, double intensity) {
        return (int) Math.round(maxHeartRate * intensity);
    }
}
