package com.cours.exemple.basics.controlflow;

/**
 * Exemple concret utilisant if/else pour simuler une décision métier.
 */
public final class CreditApprovalSimulator {

    private CreditApprovalSimulator() {
    }

    public static void example() {
        System.out.println("[Structures de contrôle] Simulation d'approbation de crédit");

        int creditScore = 680;
        double debtRatio = 0.28;
        double requestedAmount = 12_000;

        if (creditScore < 500) {
            reject("Score de crédit insuffisant");
        } else if (debtRatio > 0.4) {
            reject("Taux d'endettement trop élevé");
        } else if (requestedAmount > 20_000) {
            reject("Montant supérieur au plafond");
        } else {
            approve();
        }
    }

    private static void reject(String reason) {
        System.out.printf("Demande refusée : %s\n", reason);
    }

    private static void approve() {
        System.out.println("Demande approuvée : signature électronique envoyée.");
    }
}
