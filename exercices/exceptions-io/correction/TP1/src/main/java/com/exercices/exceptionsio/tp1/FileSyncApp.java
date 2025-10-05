package com.exercices.exceptionsio.tp1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileSyncApp {

    private FileSyncApp() {
    }

    public static void main(String[] args) throws IOException, IntegrityException {
        Path tempDir = Files.createTempDirectory("source");
        Path destDir = Files.createTempDirectory("dest");
        Path sourceFile = tempDir.resolve("mesures.txt");
        Files.writeString(sourceFile, "Mesures initiales");

        FileCopier copier = new FileCopier();
        Path destinationFile = destDir.resolve(sourceFile.getFileName());
        copier.copier(sourceFile, destinationFile);
        copier.verifierIntegrite(sourceFile, destinationFile);

        Path logFile = destDir.resolve("journal.log");
        copier.sauvegarderJournal(logFile);

        try (DirectoryWatcher watcher = new DirectoryWatcher(tempDir, copier, destDir)) {
            watcher.start(tempDir);
            Files.writeString(tempDir.resolve("nouveau.txt"), "Hello watcher");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Journal : " + copier.journal());
        System.out.println("Log écrit dans : " + logFile);
    }
}
