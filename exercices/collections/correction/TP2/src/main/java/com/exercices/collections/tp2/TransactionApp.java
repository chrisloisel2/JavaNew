package com.exercices.collections.tp2;

import java.util.List;

public final class TransactionApp {

    private TransactionApp() {
    }

    public static void main(String[] args) {
        List<Transaction> transactions = TransactionSamples.demo();
        TransactionAnalyzer.afficherRapport(transactions);
    }
}
