package com.exercices.exceptionsio.tp2;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public final class PersistenceApp {

    private PersistenceApp() {
    }

    public static void main(String[] args) throws StorageException {
        Path binaire = Path.of("emprunts.bin");
        BinaryStorage binaryStorage = new BinaryStorage(binaire);

        List<Emprunt> emprunts = List.of(
                new Emprunt(new Livre("978-2070368228", "1984", "George Orwell", LocalDate.of(1949, 6, 8)),
                        LocalDate.now(), LocalDate.now().plusDays(21)),
                new Emprunt(new Livre("978-2253006329", "Le Petit Prince", "Antoine de Saint-Exupéry", LocalDate.of(1943, 4, 6)),
                        LocalDate.now().minusDays(2), LocalDate.now().plusDays(19))
        );
        binaryStorage.sauvegarder(emprunts);
        System.out.println("Binaire chargé : " + binaryStorage.charger());

        CatalogStorage jsonStorage = new JsonStorage(new ConsoleHttpClient(), Path.of("emprunts.json"), URI.create("https://biblio.local/api"));
        jsonStorage.sauvegarder(emprunts);
        System.out.println("JSON chargé : " + jsonStorage.charger());

        try {
            Files.deleteIfExists(binaire);
            Files.deleteIfExists(Path.of("emprunts.json"));
        } catch (Exception ignored) {
        }
    }
}
