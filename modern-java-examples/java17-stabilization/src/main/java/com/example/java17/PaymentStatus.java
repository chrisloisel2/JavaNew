package com.example.java17;

/**
 * Classes scellées finalisées en Java 17.
 */
public sealed interface PaymentStatus permits PaymentStatus.Pending, PaymentStatus.Completed {

    record Pending(String reason) implements PaymentStatus { }

    record Completed(String receiptNumber) implements PaymentStatus { }
}
