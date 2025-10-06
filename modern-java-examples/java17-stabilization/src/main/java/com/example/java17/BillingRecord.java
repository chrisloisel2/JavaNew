package com.example.java17;

/**
 * Record finalisé en Java 17 LTS.
 */
public record BillingRecord(String customer, double amount) {
    public BillingRecord {
        if (amount < 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
    }
}
