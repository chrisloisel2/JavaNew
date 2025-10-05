package com.exercices.collections.tp2;

import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class TransactionAnalyzer {

    private TransactionAnalyzer() {
    }

    public static Map<String, Double> totalParCategorie(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::categorie, Collectors.summingDouble(Transaction::montant)));
    }

    public static Optional<Transaction> rechercherPlusGrande(List<Transaction> transactions, String categorie) {
        return transactions.stream()
                .filter(t -> t.categorie().equalsIgnoreCase(categorie))
                .max(java.util.Comparator.comparingDouble(Transaction::montant));
    }

    public static Deque<Transaction> detecterAnomalies(List<Transaction> transactions) {
        Deque<Transaction> anomalies = new ArrayDeque<>();
        transactions.stream()
                .collect(Collectors.groupingBy(Transaction::categorie))
                .values()
                .forEach(list -> detecterParCategorie(list, anomalies));
        return anomalies;
    }

    private static void detecterParCategorie(List<Transaction> transactions, Deque<Transaction> anomalies) {
        transactions.stream()
                .sorted(java.util.Comparator.comparing(Transaction::date))
                .forEachOrdered(transaction -> {
                    List<Transaction> fenetre = transactions.stream()
                            .filter(t -> !t.date().isBefore(transaction.date().minusDays(7))
                                    && !t.date().isAfter(transaction.date()))
                            .toList();
                    DoubleSummaryStatistics stats = fenetre.stream()
                            .collect(Collectors.summarizingDouble(Transaction::montant));
                    double moyenne = stats.getAverage();
                    if (transaction.montant() > moyenne * 1.5) {
                        anomalies.add(transaction);
                    }
                });
    }

    public static void afficherRapport(List<Transaction> transactions) {
        System.out.println("Total par catégorie : " + totalParCategorie(transactions));
        rechercherPlusGrande(transactions, "Courses").ifPresent(t ->
                System.out.printf(Locale.FRANCE, "Plus grande transaction Courses : %.2f€ le %s%n", t.montant(), t.date()));
        Deque<Transaction> anomalies = detecterAnomalies(transactions);
        if (anomalies.isEmpty()) {
            System.out.println("Aucune anomalie détectée");
        } else {
            System.out.println("Anomalies détectées (FIFO) :");
            anomalies.forEach(t -> System.out.println(" - " + t));
        }
    }
}
