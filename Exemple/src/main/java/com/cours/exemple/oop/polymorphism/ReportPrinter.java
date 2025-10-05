package com.cours.exemple.oop.polymorphism;

import java.time.LocalDate;
import java.util.List;

/**
 * Combine polymorphisme d'héritage, surcharge et redéfinition.
 */
public final class ReportPrinter {

    private ReportPrinter() {
    }

    public static void example() {
        System.out.println("[Polymorphisme] Impression polymorphe de rapports");

        List<PrintableReport> reports = List.of(
                new SalesReport(LocalDate.now()),
                new InventoryReport(LocalDate.now().minusDays(1)),
                new SalesReport(LocalDate.now().minusDays(7), "Equipe B"));

        for (PrintableReport report : reports) {
            print(report);
        }
    }

    private static void print(PrintableReport report) {
        System.out.println(report.header());
        report.printBody();
        System.out.println(report.footer());
        System.out.println();
    }

    /**
     * Contrat commun pour les rapports imprimables.
     */
    private sealed interface PrintableReport permits SalesReport, InventoryReport {
        String header();

        void printBody();

        default String footer() {
            return "--- Fin du rapport ---";
        }
    }

    private static final class SalesReport implements PrintableReport {
        private final LocalDate date;
        private final String team;

        SalesReport(LocalDate date) {
            this(date, "Equipe A");
        }

        SalesReport(LocalDate date, String team) {
            this.date = date;
            this.team = team;
        }

        @Override
        public String header() {
            return "Rapport des ventes du " + date + " (" + team + ")";
        }

        @Override
        public void printBody() {
            System.out.println("Total ventes: 42 300 €");
            System.out.println("Tickets moyens: 68 €");
        }
    }

    private static final class InventoryReport implements PrintableReport {
        private final LocalDate date;

        InventoryReport(LocalDate date) {
            this.date = date;
        }

        @Override
        public String header() {
            return "Rapport d'inventaire du " + date;
        }

        @Override
        public void printBody() {
            System.out.println("Stock critique: 5 articles");
            System.out.println("Ruptures à traiter: 2");
        }

        @Override
        public String footer() {
            return "Rappel: vérifier les réapprovisionnements en fin de journée";
        }
    }
}
