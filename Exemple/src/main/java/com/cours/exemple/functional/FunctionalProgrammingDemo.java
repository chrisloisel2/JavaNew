package com.cours.exemple.functional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Exemple complet de programmation fonctionnelle en Java à l'aide des Streams.
 * <p>
 * L'objectif de ce programme est de démontrer concrètement comment les concepts
 * fonctionnels (immutabilité, expressions lambda, fonctions d'ordre supérieur,
 * composition) se combinent avec l'API Stream pour traiter des données en Java.
 * Chaque section de {@link #main(String[])} illustre une famille d'opérations.
 */
public final class FunctionalProgrammingDemo {

    private FunctionalProgrammingDemo() {
        throw new AssertionError("Utility class");
    }

    public static void example() {
        main(new String[0]);
    }

    public static void main(String[] args) {
        System.out.println("=== PROGRAMMATION FONCTIONNELLE & STREAMS EN JAVA ===\n");

        List<Auteur> auteurs = auteurs();

        // 1. Utilisation des interfaces fonctionnelles de base ----------------
        System.out.println("1. INTERFACES FONCTIONNELLES DE BASE\n");
        Predicate<Livre> estBestSeller = livre -> livre.exemplairesVendus() > 1_000_000;
        Function<Livre, String> titreUpper = livre -> livre.titre().toUpperCase(Locale.FRENCH);
        Consumer<String> affiche = message -> System.out.println(" -> " + message);
        Supplier<String> separateur = () -> "-".repeat(40);
        BinaryOperator<Integer> addition = Integer::sum;

        System.out.println("Séparateur : " + separateur.get());
        auteurs.stream()
                .flatMap(auteur -> auteur.ouvrages().stream())
                .filter(estBestSeller)
                .map(titreUpper)
                .forEach(affiche);
        System.out.println("2 + 2 = " + addition.apply(2, 2));

        // 2. Construction et transformation de Streams -----------------------
        System.out.println("\n2. CONSTRUCTION ET TRANSFORMATION DE STREAMS\n");
        List<Livre> livres = auteurs.stream().flatMap(auteur -> auteur.ouvrages().stream()).toList();

        Stream<Livre> streamDeCollection = livres.stream();
        Stream<Livre> streamDeValeurs = Stream.of(
                new Livre("Programmation réactive", Genre.TECHNIQUE, 2023, 45.0, 12_000),
                new Livre("Architecture Hexagonale", Genre.TECHNIQUE, 2020, 39.0, 80_000)
        );
        Stream<Integer> streamInfini = Stream.iterate(0, n -> n + 1).limit(5);

        System.out.println("Stream de collection -> taille: " + streamDeCollection.count());
        System.out.println("Stream de valeurs -> titres: " + streamDeValeurs.map(Livre::titre).toList());
        System.out.println("Stream infini itéré -> " + streamInfini.toList());

        // 3. Opérations intermédiaires ---------------------------------------
        System.out.println("\n3. OPERATIONS INTERMEDIAIRES\n");
        List<String> titresFiltresOrdonnes = livres.stream()
                .filter(livre -> livre.prix() >= 30)
                .distinct()
                .sorted(Comparator.comparing(Livre::annee).reversed())
                .map(Livre::titre)
                .peek(titre -> System.out.println("Aperçu: " + titre))
                .skip(1)
                .limit(3)
                .toList();
        System.out.println("Titres filtrés/ordonnés: " + titresFiltresOrdonnes);

        // 4. Opérations terminales -------------------------------------------
        System.out.println("\n4. OPERATIONS TERMINALES\n");
        long nbLivresTechniques = livres.stream().filter(livre -> livre.genre() == Genre.TECHNIQUE).count();
        Optional<Livre> livrePlusRecent = livres.stream().max(Comparator.comparing(Livre::annee));
        double prixTotal = livres.stream().mapToDouble(Livre::prix).sum();
        double prixMoyen = livres.stream().mapToDouble(Livre::prix).average().orElse(0);
        boolean tousApres2000 = livres.stream().allMatch(livre -> livre.annee() >= 2000);
        boolean aucunAvant1980 = livres.stream().noneMatch(livre -> livre.annee() < 1980);

        System.out.println("Nombre de livres techniques : " + nbLivresTechniques);
        livrePlusRecent.ifPresent(livre -> System.out.println("Livre le plus récent : " + livre.titre()));
        System.out.println("Prix total : " + prixTotal + " €, prix moyen : " + prixMoyen + " €");
        System.out.println("Tous après 2000 ? " + tousApres2000);
        System.out.println("Aucun avant 1980 ? " + aucunAvant1980);

        // 5. Collectors avancés ----------------------------------------------
        System.out.println("\n5. COLLECTORS AVANCES\n");
        Map<Genre, List<Livre>> livresParGenre = livres.stream()
                .collect(Collectors.groupingBy(Livre::genre));

        Map<Genre, Set<String>> titresParGenre = livres.stream()
                .collect(Collectors.groupingBy(
                        Livre::genre,
                        Collectors.mapping(Livre::titre, Collectors.toSet())
                ));

        Map<Boolean, List<Livre>> partitionVentes = livres.stream()
                .collect(Collectors.partitioningBy(estBestSeller));

        Map<String, Double> prixParTitre = livres.stream()
                .collect(Collectors.toMap(Livre::titre, Livre::prix, (p1, p2) -> p1));

        Collector<Livre, ?, String> collectorTitres = Collectors.collectingAndThen(
                Collectors.mapping(Livre::titre, Collectors.joining(", ")),
                titres -> "Titres: " + titres
        );
        String listeTitres = livres.stream().collect(collectorTitres);

        IntSummaryStatistics statsPrix = livres.stream()
                .collect(Collectors.summarizingInt(livre -> (int) livre.prix()));

        DoubleSummaryStatistics statsVentes = livres.stream()
                .collect(Collectors.summarizingDouble(Livre::exemplairesVendus));

        Double valeurCombinee = livres.stream().collect(Collectors.teeing(
                Collectors.averagingDouble(Livre::prix),
                Collectors.summingDouble(Livre::exemplairesVendus),
                (moyPrix, totalVentes) -> moyPrix * Math.log10(totalVentes)
        ));

        System.out.println("Livres par genre : " + livresParGenre);
        System.out.println("Titres par genre : " + titresParGenre);
        System.out.println("Partition best-sellers : " + partitionVentes.keySet());
        System.out.println("Prix par titre : " + prixParTitre);
        System.out.println(listeTitres);
        System.out.println("Stats prix : " + statsPrix);
        System.out.println("Stats ventes : " + statsVentes);
        System.out.println("Valeur combinée : " + valeurCombinee);

        // 6. Réductions, immutabilité et Optional ----------------------------
        System.out.println("\n6. REDUCTIONS ET OPTIONAL\n");
        Optional<Livre> bestSellerLePlusAncien = livres.stream()
                .filter(estBestSeller)
                .reduce((livre1, livre2) -> livre1.annee() <= livre2.annee() ? livre1 : livre2);

        int ventesTotales = livres.stream()
                .map(Livre::exemplairesVendus)
                .reduce(0, Integer::sum);

        Optional<String> titreLePlusLong = livres.stream()
                .map(Livre::titre)
                .max(Comparator.comparingInt(String::length));

        bestSellerLePlusAncien.ifPresent(livre -> System.out.println("Best-seller le plus ancien : " + livre.titre()));
        System.out.println("Ventes totales : " + ventesTotales);
        System.out.println("Titre le plus long : " + titreLePlusLong.orElse("Aucun"));

        // 7. Composition de fonctions ---------------------------------------
        System.out.println("\n7. COMPOSITION DE FONCTIONS\n");
        Function<Livre, Livre> appliquerPromo = livre -> livre.withPrix(livre.prix() * 0.9);
        Function<Livre, Livre> ajouterBadge = livre -> livre.withBadge("⭐ " + livre.titre());
        Function<Livre, Livre> pipeline = appliquerPromo.andThen(ajouterBadge);

        List<Livre> livresTransformes = livres.stream()
                .map(pipeline)
                .toList();

        System.out.println("Livres transformés : " + livresTransformes);

        // 8. Stream parallèles -----------------------------------------------
        System.out.println("\n8. STREAMS PARALLELES\n");
        long tempsDebut = System.nanoTime();
        int totalParallele = livres.parallelStream()
                .mapToInt(Livre::exemplairesVendus)
                .sum();
        long tempsFin = System.nanoTime();
        System.out.printf("Total des ventes (parallelStream) : %d (calculé en %.2f ms)%n",
                totalParallele, (tempsFin - tempsDebut) / 1_000_000.0);

        // 9. Streams primitifs et transformation inverse --------------------
        System.out.println("\n9. STREAMS PRIMITIFS\n");
        IntStream annees = livres.stream().mapToInt(Livre::annee);
        int min = annees.min().orElseThrow();
        double moyenne = livres.stream().mapToInt(Livre::annee).average().orElse(0);
        List<Livre> livresRecents = livres.stream()
                .mapToInt(Livre::annee)
                .map(annee -> annee + 1)
                .mapToObj(annee -> new Livre("Année " + annee, Genre.AUTRE, annee, 10, 0))
                .limit(3)
                .toList();

        System.out.println("Année minimum : " + min);
        System.out.println("Année moyenne : " + moyenne);
        System.out.println("Livres récents artificiels : " + livresRecents);

        // 10. FlatMap avancé -------------------------------------------------
        System.out.println("\n10. FLATMAP AVANCE\n");
        List<String> collaborations = auteurs.stream()
                .flatMap(auteur -> auteur.collaborations().stream()
                        .flatMap(collaboration -> collaboration.ouvrages().stream()
                                .map(livre -> auteur.nom() + " & " + collaboration.nom() + " -> " + livre.titre())))
                .toList();
        System.out.println("Collaborations : " + collaborations);

        System.out.println("\n=== FIN DEMO ===");
    }

    private static List<Auteur> auteurs() {
        Auteur martin = new Auteur(
                "Robert C. Martin",
                LocalDate.of(1952, 12, 5),
                List.of(
                        new Livre("Clean Code", Genre.TECHNIQUE, 2008, 42.0, 2_500_000),
                        new Livre("Clean Architecture", Genre.TECHNIQUE, 2017, 45.0, 800_000),
                        new Livre("Clean Agile", Genre.TECHNIQUE, 2019, 38.0, 200_000)
                ),
                List.of()
        );

        Auteur fowler = new Auteur(
                "Martin Fowler",
                LocalDate.of(1963, 12, 18),
                List.of(
                        new Livre("Refactoring", Genre.TECHNIQUE, 1999, 55.0, 1_200_000),
                        new Livre("Patterns of Enterprise Application Architecture", Genre.TECHNIQUE, 2002, 60.0, 600_000)
                ),
                List.of(martin)
        );

        Auteur meyer = new Auteur(
                "Stephenie Meyer",
                LocalDate.of(1973, 12, 24),
                List.of(
                        new Livre("Twilight", Genre.ROMAN, 2005, 19.0, 10_000_000),
                        new Livre("New Moon", Genre.ROMAN, 2006, 19.0, 7_000_000)
                ),
                List.of()
        );

        Auteur sanderson = new Auteur(
                "Brandon Sanderson",
                LocalDate.of(1975, 12, 19),
                List.of(
                        new Livre("Mistborn", Genre.ROMAN, 2006, 24.0, 4_000_000),
                        new Livre("The Way of Kings", Genre.ROMAN, 2010, 29.0, 3_000_000),
                        new Livre("Warbreaker", Genre.ROMAN, 2009, 22.0, 1_200_000)
                ),
                List.of()
        );

        Auteur brown = new Auteur(
                "Dan Brown",
                LocalDate.of(1964, 6, 22),
                List.of(
                        new Livre("The Da Vinci Code", Genre.ROMAN, 2003, 20.0, 8_000_000),
                        new Livre("Angels & Demons", Genre.ROMAN, 2000, 18.0, 5_000_000),
                        new Livre("Inferno", Genre.ROMAN, 2013, 21.0, 2_500_000)
                ),
                List.of(meyer)
        );

        Auteur robin = new Auteur(
                "Robin Hobb",
                LocalDate.of(1952, 3, 5),
                List.of(
                        new Livre("Assassin's Apprentice", Genre.ROMAN, 1995, 15.0, 1_500_000),
                        new Livre("Fool's Errand", Genre.ROMAN, 2001, 17.0, 900_000)
                ),
                List.of(sanderson)
        );

        return List.of(martin, fowler, meyer, sanderson, brown, robin);
    }

    private enum Genre {
        TECHNIQUE, ROMAN, AUTRE
    }

    private record Livre(String titre, Genre genre, int annee, double prix, int exemplairesVendus, String badge) {

        private Livre(String titre, Genre genre, int annee, double prix, int exemplairesVendus) {
            this(titre, genre, annee, prix, exemplairesVendus, "");
        }

        private Livre withPrix(double nouveauPrix) {
            return new Livre(titre, genre, annee, nouveauPrix, exemplairesVendus, badge);
        }

        private Livre withBadge(String nouveauBadge) {
            return new Livre(titre, genre, annee, prix, exemplairesVendus, nouveauBadge);
        }

        @Override
        public String toString() {
            return (badge.isBlank() ? titre : badge) + " (" + genre + ", " + annee + ", " + prix + " €, " + exemplairesVendus + " ex.)";
        }
    }

    private record Auteur(String nom, LocalDate dateNaissance, List<Livre> ouvrages, List<Auteur> collaborations) {
    }
}
