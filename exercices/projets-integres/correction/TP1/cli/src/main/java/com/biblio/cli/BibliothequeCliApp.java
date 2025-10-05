package com.biblio.cli;

import com.biblio.core.CatalogueEvent;
import com.biblio.core.CatalogueService;
import com.biblio.core.Emprunt;
import com.biblio.core.Livre;
import com.biblio.core.Utilisateur;
import com.biblio.storage.FileStorage;
import com.biblio.storage.MemoryStorage;
import com.biblio.storage.RemoteSynchronizer;
import com.biblio.storage.StorageException;
import com.biblio.storage.StubHttpClient;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class BibliothequeCliApp {

    private BibliothequeCliApp() {
    }

    public static void main(String[] args) throws StorageException {
        CatalogueService catalogue = new CatalogueService();
        MemoryStorage memoryStorage = new MemoryStorage();
        FileStorage fileStorage = new FileStorage(Path.of("catalogue.txt"));
        RemoteSynchronizer synchronizer = new RemoteSynchronizer(new StubHttpClient(), java.net.URI.create("https://biblio.local/api"));

        afficherMenu();
        scenarioDemonstration(catalogue, memoryStorage, fileStorage, synchronizer);
    }

    private static void afficherMenu() {
        String menu = """
                ============================
                Bibliothèque – Menu principal
                1. Ajouter un livre
                2. Rechercher un livre
                3. Enregistrer un emprunt
                4. Générer un rapport
                ============================
                """;
        System.out.println(menu);
    }

    private static void scenarioDemonstration(CatalogueService catalogue, MemoryStorage memoryStorage,
                                              FileStorage fileStorage, RemoteSynchronizer synchronizer) throws StorageException {
        Livre livre = new Livre("978-0134685991", "Effective Java", "Joshua Bloch", LocalDate.of(2018, 1, 6));
        catalogue.ajouterLivre(livre);
        catalogue.mettreAJourLivre(livre.isbn(), current -> new Livre(current.isbn(), current.titre() + " (3e éd.)", current.auteur(), current.parution()));

        Utilisateur utilisateur = new Utilisateur("U1", "Alice", "alice@example.com");
        Emprunt emprunt = new Emprunt(livre, utilisateur, LocalDate.now(), LocalDate.now().plusDays(21));
        System.out.println("Emprunt enregistré : " + emprunt);

        List<Livre> livres = catalogue.tous();
        memoryStorage.sauvegarder(livres);
        fileStorage.sauvegarder(livres);
        synchronizer.envoyer(livres);

        genererRapport(catalogue, List.of(emprunt));
        afficherHistorique(catalogue.events());
    }

    private static void genererRapport(CatalogueService catalogue, List<Emprunt> emprunts) {
        Map<String, Long> empruntsParUtilisateur = emprunts.stream()
                .collect(Collectors.groupingBy(emprunt -> emprunt.utilisateur().nom(), Collectors.counting()));
        String rapport = """
                Rapport d'activité
                ------------------
                Livres en catalogue : %d
                Emprunts par utilisateur : %s
                """.formatted(catalogue.tous().size(), empruntsParUtilisateur);
        System.out.println(rapport);
    }

    private static void afficherHistorique(List<CatalogueEvent> events) {
        System.out.println("Historique des événements :");
        events.forEach(event -> System.out.println(" - " + event));
    }
}
