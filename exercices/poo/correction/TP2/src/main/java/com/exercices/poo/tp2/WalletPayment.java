package com.exercices.poo.tp2;

public record WalletPayment(String walletId, double balance) implements PaymentMethod {
    @Override
    public boolean process(double amount) {
        if (balance < amount) {
            throw new IllegalStateException("Solde insuffisant");
        }
        System.out.printf("Paiement portefeuille de %.2f€ validé.%n", amount);
        return true;
    }
}
