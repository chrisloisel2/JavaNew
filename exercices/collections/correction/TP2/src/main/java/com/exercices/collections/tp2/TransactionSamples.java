package com.exercices.collections.tp2;

import java.time.LocalDate;
import java.util.List;

public final class TransactionSamples {

    private TransactionSamples() {
    }

    public static List<Transaction> demo() {
        LocalDate today = LocalDate.now();
        return List.of(
                new Transaction("T1", "Courses", 82.5, today.minusDays(1)),
                new Transaction("T2", "Loisirs", 45.0, today.minusDays(3)),
                new Transaction("T3", "Courses", 23.7, today.minusDays(2)),
                new Transaction("T4", "Transport", 60.0, today.minusDays(5)),
                new Transaction("T5", "Courses", 120.0, today.minusDays(6)),
                new Transaction("T6", "Transport", 15.0, today.minusDays(8))
        );
    }
}
