package com.biblio.storage;

import com.biblio.core.Livre;

import java.util.List;

public interface CatalogueStorage {
    void sauvegarder(List<Livre> livres) throws StorageException;
    List<Livre> charger() throws StorageException;
}
