package com.biblio.storage;

import com.biblio.core.Livre;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public final class FileStorage implements CatalogueStorage {

    private final Path fichier;

    public FileStorage(Path fichier) {
        this.fichier = fichier;
    }

    @Override
    public void sauvegarder(List<Livre> livres) throws StorageException {
        String contenu = livres.stream()
                .map(livre -> String.join(";", livre.isbn(), livre.titre(), livre.auteur(), livre.parution().toString()))
                .collect(Collectors.joining(System.lineSeparator()));
        try {
            Files.writeString(fichier, contenu);
        } catch (IOException e) {
            throw new StorageException("Impossible d'écrire le fichier", e);
        }
    }

    @Override
    public List<Livre> charger() throws StorageException {
        if (!Files.exists(fichier)) {
            return List.of();
        }
        try {
            return Files.readAllLines(fichier).stream()
                    .filter(line -> !line.isBlank())
                    .map(line -> line.split(";"))
                    .map(parts -> new Livre(parts[0], parts[1], parts[2], java.time.LocalDate.parse(parts[3])))
                    .toList();
        } catch (IOException e) {
            throw new StorageException("Lecture du fichier impossible", e);
        }
    }
}
