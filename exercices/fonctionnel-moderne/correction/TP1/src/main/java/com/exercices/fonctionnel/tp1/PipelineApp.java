package com.exercices.fonctionnel.tp1;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.DoubleSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

public final class PipelineApp {

    private PipelineApp() {
    }

    public static void main(String[] args) {
        Supplier<Double> generator = () -> ThreadLocalRandom.current().nextDouble(0, 50);
        List<Double> mesures = Stream.generate(generator).limit(100).toList();
        System.out.println("Mesures générées : " + mesures.size());

        Predicate<Double> filtreIntervalle = value -> value >= 5 && value <= 45;
        Function<Double, BigDecimal> arrondi = value -> BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);

        List<BigDecimal> nettoyees = mesures.stream()
                .filter(filtreIntervalle)
                .map(arrondi)
                .toList();

        DoubleSummaryStatistics stats = nettoyees.stream()
                .mapToDouble(BigDecimal::doubleValue)
                .summaryStatistics();
        System.out.println("Statistiques : " + stats);

        Collector<BigDecimal, ?, Map<String, Long>> trancheCollector = Collector.of(
                LinkedHashMap::new,
                (map, valeur) -> map.merge(tranchePour(valeur), 1L, Long::sum),
                (left, right) -> {
                    right.forEach((key, value) -> left.merge(key, value, Long::sum));
                    return left;
                }
        );

        Map<String, Long> histogramme = nettoyees.stream().collect(trancheCollector);
        ConsumerAffichage.afficher(histogramme);
    }

    private static String tranchePour(BigDecimal valeur) {
        int intervalle = valeur.intValue() / 10;
        int min = intervalle * 10;
        int max = min + 10;
        return min + "-" + max;
    }

    private static final class ConsumerAffichage {
        private static void afficher(Map<String, Long> histogramme) {
            histogramme.forEach((tranche, count) -> {
                String bar = "#".repeat(Math.toIntExact(count));
                System.out.println(tranche + " : " + bar);
            });
        }
    }
}
