package com.biblio.storage;

import com.biblio.core.Livre;

import java.util.ArrayList;
import java.util.List;

public final class MemoryStorage implements CatalogueStorage {

    private final List<Livre> livres = new ArrayList<>();

    @Override
    public void sauvegarder(List<Livre> livres) {
        this.livres.clear();
        this.livres.addAll(livres);
    }

    @Override
    public List<Livre> charger() {
        return List.copyOf(livres);
    }
}
