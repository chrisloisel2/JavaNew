package com.exercices.exceptionsio.tp2;

import java.util.List;

public interface CatalogStorage {
    void sauvegarder(List<Emprunt> emprunts) throws StorageException;
    List<Emprunt> charger() throws StorageException;
}
