package com.exercices.exceptionsio.tp2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

final class JsonSupport {

    private JsonSupport() {
    }

    static List<Emprunt> fromJson(String json) {
        if (json == null || json.isBlank()) {
            return List.of();
        }
        List<Emprunt> emprunts = new ArrayList<>();
        String content = json.substring(1, json.length() - 1).trim();
        if (content.isEmpty()) {
            return List.of();
        }
        for (String entry : content.split("},")) {
            String normalized = entry.replace("{", "").replace("}", "");
            String[] parts = normalized.split(",");
            String isbn = valueOf(parts[0]);
            String titre = valueOf(parts[1]);
            String auteur = valueOf(parts[2]);
            LocalDate parution = LocalDate.parse(valueOf(parts[3]));
            LocalDate debut = LocalDate.parse(valueOf(parts[4]));
            LocalDate fin = LocalDate.parse(valueOf(parts[5]));
            Livre livre = new Livre(isbn, titre, auteur, parution);
            emprunts.add(new Emprunt(livre, debut, fin));
        }
        return emprunts;
    }

    private static String valueOf(String part) {
        return part.substring(part.indexOf(':') + 2, part.length() - 1);
    }
}
