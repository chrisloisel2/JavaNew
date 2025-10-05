package com.exercices.collections.tp2;

import java.time.LocalDate;

public record Transaction(String id, String categorie, double montant, LocalDate date) {
}
