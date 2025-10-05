package com.exercices.exceptionsio.tp2;

import java.io.Serializable;
import java.time.LocalDate;

public record Emprunt(Livre livre, LocalDate debut, LocalDate fin) implements Serializable {
}
