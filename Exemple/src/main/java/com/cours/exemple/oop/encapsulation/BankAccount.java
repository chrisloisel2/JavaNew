package com.cours.exemple.oop.encapsulation;

import java.math.BigDecimal;

/**
 * Encapsule l'état d'un compte bancaire en exposant des accesseurs contrôlés.
 */
public final class BankAccount {
    private BigDecimal balance = BigDecimal.valueOf(1500);

    private BankAccount() {
    }

    public static void example() {
        System.out.println("[Encapsulation] Compte bancaire sécurisé");
        BankAccount account = new BankAccount();
        account.deposit(BigDecimal.valueOf(250));
        account.withdraw(BigDecimal.valueOf(90));
        System.out.println("Solde final: " + account.getBalance() + " €");
    }

    public BigDecimal getBalance() {
        return balance;
    }

    private void deposit(BigDecimal amount) {
        requirePositive(amount);
        balance = balance.add(amount);
    }

    private void withdraw(BigDecimal amount) {
        requirePositive(amount);
        if (balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Solde insuffisant");
        }
        balance = balance.subtract(amount);
    }

    private void requirePositive(BigDecimal amount) {
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
    }
}
