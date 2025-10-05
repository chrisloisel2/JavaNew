package com.exercices.exceptionsio.tp2;

import java.io.Serializable;
import java.time.LocalDate;

public record Livre(String isbn, String titre, String auteur, LocalDate parution) implements Serializable {
}
