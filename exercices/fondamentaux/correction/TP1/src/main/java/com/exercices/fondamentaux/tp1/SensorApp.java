package com.exercices.fondamentaux.tp1;

import java.time.Instant;
import java.util.Locale;
import java.util.Scanner;

/**
 * Point d'entrée de démonstration pour le TP1.
 */
public final class SensorApp {

    private SensorApp() {
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.FRANCE);
        try (Scanner scanner = new Scanner(System.in)) {
            char reponse;
            do {
                Sensor sensor = new Sensor(42, "Laboratoire - Toit", 18.5, 22.0f, true, 2024001L, Instant.now());
                System.out.println(sensor.toSummary());

                double[] mesures = MeasurementSimulator.simulateDay(sensor.derniereMesure(), sensor.seuilCritique());
                System.out.println("Mesures horaires : " + MeasurementSimulator.toList(mesures));

                SensorStatus ifElseStatus = AlertEvaluator.evaluateWithIfElse(MeasurementSimulator.highestMeasurement(mesures), sensor.seuilCritique());
                SensorStatus switchStatus = AlertEvaluator.evaluateWithSwitch(MeasurementSimulator.highestMeasurement(mesures), sensor.seuilCritique());
                System.out.printf("Statut via if/else : %s | via switch : %s%n", ifElseStatus, switchStatus);

                System.out.println(DailyReportGenerator.generateReport(sensor, mesures));

                System.out.print("Relancer la simulation ? (o/n) : ");
                String input = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
                reponse = input.isEmpty() ? 'n' : input.charAt(0);
            } while (reponse == 'o');
        }
    }
}
