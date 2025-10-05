package com.biblio.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

public final class CatalogueService {

    private final List<Livre> livres = new ArrayList<>();
    private final List<CatalogueEvent> events = new ArrayList<>();

    public void ajouterLivre(Livre livre) {
        livres.add(livre);
        events.add(new LivreCree(livre));
    }

    public void mettreAJourLivre(String isbn, UnaryOperator<Livre> updater) {
        Optional<Livre> existant = trouverParIsbn(isbn);
        existant.ifPresent(livre -> {
            Livre misAJour = updater.apply(livre);
            livres.remove(livre);
            livres.add(misAJour);
            events.add(new LivreMisAJour(misAJour));
        });
    }

    public void supprimerLivre(String isbn) {
        trouverParIsbn(isbn).ifPresent(livre -> {
            livres.remove(livre);
            events.add(new LivreSupprime(livre));
        });
    }

    public Optional<Livre> trouverParIsbn(String isbn) {
        return livres.stream().filter(livre -> livre.isbn().equals(isbn)).findFirst();
    }

    public List<Livre> tous() {
        return Collections.unmodifiableList(livres);
    }

    public List<CatalogueEvent> events() {
        return Collections.unmodifiableList(events);
    }
}
