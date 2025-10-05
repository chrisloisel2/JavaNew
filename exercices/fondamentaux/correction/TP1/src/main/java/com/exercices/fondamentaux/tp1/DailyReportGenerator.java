package com.exercices.fondamentaux.tp1;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.DoubleSummaryStatistics;
import java.util.Locale;

final class DailyReportGenerator {

    private DailyReportGenerator() {
    }

    static String generateReport(Sensor sensor, double[] measures) {
        DoubleSummaryStatistics stats = MeasurementSimulator.summarize(measures);
        SensorStatus status = AlertEvaluator.evaluateWithIfElse(stats.getAverage(), sensor.seuilCritique());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.FRANCE);

        return String.format("""
                Rapport du %s
                Capteur : %s
                Moyenne journalière : %.2f°C
                Mesure maximale : %.2f°C
                Dépassement de seuil : %s
                Statut moyen : %s
                """,
                formatter.format(Instant.now()),
                sensor.emplacement(),
                stats.getAverage(),
                stats.getMax(),
                MeasurementSimulator.hasThresholdBreach(measures, sensor.seuilCritique()) ? "oui" : "non",
                status);
    }
}
