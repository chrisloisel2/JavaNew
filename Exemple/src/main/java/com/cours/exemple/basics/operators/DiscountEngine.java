package com.cours.exemple.basics.operators;

/**
 * Met en évidence l'utilisation d'opérateurs arithmétiques, relationnels et logiques.
 */
public final class DiscountEngine {

    private DiscountEngine() {
    }

    public static void example() {
        System.out.println("[Opérateurs] Calcul de remise commerciale");

        double purchaseAmount = 180.0;
        boolean isBlackFriday = true;
        int loyaltyYears = 3;

        double discount = 0;

        if (purchaseAmount > 200 || isBlackFriday) {
            discount += 0.15;
        }

        if (loyaltyYears >= 3 && !isBlackFriday) {
            discount += 0.05;
        }

        double total = purchaseAmount - purchaseAmount * discount;
        System.out.printf("Remise appliquée: %.0f%% - Total à payer: %.2f €\n", discount * 100, total);
    }
}
