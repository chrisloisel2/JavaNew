package com.biblio.core;

import java.time.LocalDate;

public record Livre(String isbn, String titre, String auteur, LocalDate parution) {
}
