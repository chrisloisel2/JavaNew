package com.exercices.exceptionsio.tp2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class BinaryStorage {

    private final Path fichier;

    public BinaryStorage(Path fichier) {
        this.fichier = fichier;
    }

    public void sauvegarder(List<Emprunt> emprunts) throws StorageException {
        try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(fichier))) {
            output.writeObject(new ArrayList<>(emprunts));
        } catch (IOException e) {
            throw new StorageException("Impossible de sauvegarder", e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Emprunt> charger() throws StorageException {
        if (!Files.exists(fichier)) {
            return List.of();
        }
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(fichier))) {
            return (List<Emprunt>) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new StorageException("Impossible de charger", e);
        }
    }
}
