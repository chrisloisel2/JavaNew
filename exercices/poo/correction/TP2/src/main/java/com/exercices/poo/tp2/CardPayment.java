package com.exercices.poo.tp2;

import java.time.YearMonth;

public record CardPayment(String cardNumber, YearMonth expiry) implements PaymentMethod {
    @Override
    public boolean process(double amount) {
        if (cardNumber == null || cardNumber.length() < 12) {
            throw new IllegalArgumentException("Numéro de carte invalide");
        }
        if (YearMonth.now().isAfter(expiry)) {
            throw new IllegalStateException("Carte expirée");
        }
        System.out.printf("Paiement carte de %.2f€ validé.%n", amount);
        return true;
    }
}
