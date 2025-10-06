package com.example.java17;

/**
 * Switch expressions finalisées.
 */
public class SwitchPatterns {

    public String describeStatus(PaymentStatus status) {
        return switch (status) {
            case PaymentStatus.Pending pending -> "En attente: " + pending.reason();
            case PaymentStatus.Completed completed -> "Terminé: " + completed.receiptNumber();
        };
    }
}
