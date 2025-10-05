package com.cours.exemple.principles.kiss;

/**
 * Met en avant une implémentation volontairement simple.
 */
public final class BudgetCalculator {

    private BudgetCalculator() {
    }

    public static void example() {
        System.out.println("[KISS] Calculatrice de budget personnel");

        double income = 2_300;
        double rent = 780;
        double groceries = 280;
        double hobbies = 150;

        double savings = calculateSavings(income, rent + groceries + hobbies);
        System.out.printf("Epargne mensuelle estimée: %.2f €\n", savings);
    }

    private static double calculateSavings(double income, double expenses) {
        return income - expenses;
    }
}
