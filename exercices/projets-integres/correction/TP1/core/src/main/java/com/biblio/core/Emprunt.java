package com.biblio.core;

import java.time.LocalDate;

public record Emprunt(Livre livre, Utilisateur utilisateur, LocalDate dateDebut, LocalDate dateFin) {
}
