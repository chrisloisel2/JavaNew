package com.exercices.fondamentaux.tp1;

import java.security.SecureRandom;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.DoubleStream;

final class MeasurementSimulator {

    private static final SecureRandom RANDOM = new SecureRandom();

    private MeasurementSimulator() {
    }

    static double[] simulateDay(double base, float seuil) {
        return DoubleStream.iterate(base, prev -> prev + RANDOM.nextGaussian())
                .limit(24)
                .map(value -> Math.max(value, -20))
                .map(value -> value + RANDOM.nextDouble(1.5))
                .toArray();
    }

    static DoubleSummaryStatistics summarize(double[] values) {
        return DoubleStream.of(values).summaryStatistics();
    }

    static boolean hasThresholdBreach(double[] values, float seuil) {
        return DoubleStream.of(values).anyMatch(value -> value >= seuil);
    }

    static double highestMeasurement(double[] values) {
        return DoubleStream.of(values).max().orElse(Double.NaN);
    }

    static List<Double> toList(double[] values) {
        return DoubleStream.of(values).boxed().toList();
    }
}
