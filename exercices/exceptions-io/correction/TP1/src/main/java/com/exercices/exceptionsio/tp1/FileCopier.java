package com.exercices.exceptionsio.tp1;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class FileCopier {

    private static final Logger LOGGER = Logger.getLogger(FileCopier.class.getName());
    private final List<String> journal = new ArrayList<>();

    public void copier(Path source, Path destination) {
        try (Reader reader = Files.newBufferedReader(source);
             Writer writer = Files.newBufferedWriter(destination)) {
            reader.transferTo(writer);
            journal.add("Copie réussie : " + source + " -> " + destination);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur de copie", e);
            journal.add("Copie échouée : " + source + " -> " + destination + " : " + e.getMessage());
        }
    }

    public boolean verifierIntegrite(Path source, Path destination) throws IntegrityException {
        try {
            long tailleSource = Files.size(source);
            long tailleDestination = Files.size(destination);
            if (tailleSource != tailleDestination) {
                throw new IntegrityException("Tailles différentes");
            }
            String hashSource = calculerHash(source);
            String hashDestination = calculerHash(destination);
            if (!hashSource.equals(hashDestination)) {
                throw new IntegrityException("Hash différents");
            }
            return true;
        } catch (IOException e) {
            throw new IntegrityException("Impossible de vérifier l'intégrité : " + e.getMessage());
        }
    }

    private String calculerHash(Path path) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (var stream = Files.newInputStream(path)) {
                byte[] buffer = stream.readAllBytes();
                byte[] hash = digest.digest(buffer);
                return HexFormat.of().formatHex(hash);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("Algorithme SHA-256 indisponible", e);
        }
    }

    public void sauvegarderJournal(Path logPath) {
        try {
            Files.write(logPath, journal);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Impossible d'écrire le journal", e);
        }
    }

    public List<String> journal() {
        return List.copyOf(journal);
    }
}
