package com.example.kickstarter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * Jeu de données statique destiné aux exercices sur les streams.
 */
public final class KickstarterData {

    public static final List<Project> PROJECTS;
    public static final List<Backer> BACKERS;
    public static final List<Pledge> PLEDGES;
    public static final Map<UUID, Project> PROJECT_BY_ID;
    public static final Map<UUID, Backer> BACKER_BY_ID;

    static {
        var projects = new ArrayList<Project>();

        var modularDesk = new Project(
                UUID.fromString("0c8b2d8b-15c0-4ef0-a54a-f1c2de26f4da"),
                "Modular Desk System",
                Category.DESIGN,
                "US",
                "Portland",
                85_000,
                123_500,
                LocalDate.of(2023, Month.SEPTEMBER, 12),
                LocalDate.of(2023, Month.OCTOBER, 27),
                "Studio Timber",
                Set.of("workspace", "sustainable", "productivity"),
                List.of(
                        new Reward("Sticker pack", 15, false, LocalDate.of(2024, Month.JANUARY, 15), "Stickers recyclés"),
                        new Reward("Desk starter kit", 99, false, LocalDate.of(2024, Month.FEBRUARY, 28), "Modules de base"),
                        new Reward("Complete setup", 259, true, LocalDate.of(2024, Month.APRIL, 30), "Configuration complète")
                )
        );
        projects.add(modularDesk);

        var indieAdventure = new Project(
                UUID.fromString("fdfb6eb2-7a3a-4cf0-b7b8-8f2f3a4006f7"),
                "Indie Adventure Game",
                Category.GAMES,
                "CA",
                "Vancouver",
                65_000,
                72_450,
                LocalDate.of(2024, Month.JANUARY, 8),
                LocalDate.of(2024, Month.MARCH, 2),
                "Pixel Sprouts",
                Set.of("metroidvania", "pixel-art", "soundtrack"),
                List.of(
                        new Reward("Digital copy", 20, false, LocalDate.of(2024, Month.JUNE, 1), "Jeu + fond d'écran"),
                        new Reward("Collector edition", 80, true, LocalDate.of(2024, Month.JULY, 15), "OST + artbook"),
                        new Reward("Design workshop", 150, true, LocalDate.of(2024, Month.AUGUST, 10), "Session live avec l'équipe")
                )
        );
        projects.add(indieAdventure);

        var fermentationKit = new Project(
                UUID.fromString("11e6a132-0fb4-4b7e-a89a-2bce845fc98f"),
                "Artisanal Fermentation Kit",
                Category.FOOD,
                "FR",
                "Lyon",
                30_000,
                44_280,
                LocalDate.of(2023, Month.NOVEMBER, 4),
                LocalDate.of(2023, Month.DECEMBER, 20),
                "Ferments & Co",
                Set.of("kombucha", "zero-waste", "starter"),
                List.of(
                        new Reward("Recettes PDF", 12, false, LocalDate.of(2024, Month.JANUARY, 5), "12 recettes exclusives"),
                        new Reward("Kit découverte", 39, false, LocalDate.of(2024, Month.FEBRUARY, 1), "Materiel + cultures"),
                        new Reward("Masterclass", 95, true, LocalDate.of(2024, Month.MARCH, 18), "Atelier en présentiel")
                )
        );
        projects.add(fermentationKit);

        var synthwaveVinyl = new Project(
                UUID.fromString("3d53a2e4-6115-4c68-baf0-6de97c1ae499"),
                "Synthwave Vinyl Collection",
                Category.MUSIC,
                "DE",
                "Berlin",
                42_000,
                39_120,
                LocalDate.of(2024, Month.FEBRUARY, 20),
                LocalDate.of(2024, Month.APRIL, 5),
                "Neon Pulse",
                Set.of("vinyl", "retro", "limited"),
                List.of(
                        new Reward("Digital album", 18, false, LocalDate.of(2024, Month.APRIL, 20), "Album numérique"),
                        new Reward("Vinyl standard", 35, false, LocalDate.of(2024, Month.JUNE, 12), "Vinyl simple"),
                        new Reward("Vinyl deluxe", 65, true, LocalDate.of(2024, Month.JULY, 8), "Vinyl double avec art prints")
                )
        );
        projects.add(synthwaveVinyl);

        var ecoBackpack = new Project(
                UUID.fromString("dd8b1dcd-71d4-4f61-8090-046d220c0e4f"),
                "Eco-smart Backpack",
                Category.TECHNOLOGY,
                "US",
                "San Francisco",
                110_000,
                158_900,
                LocalDate.of(2023, Month.OCTOBER, 2),
                LocalDate.of(2023, Month.NOVEMBER, 18),
                "NovaGear Labs",
                Set.of("smart", "solar", "travel"),
                List.of(
                        new Reward("Early backer", 119, true, LocalDate.of(2024, Month.JUNE, 10), "Sac à prix réduit"),
                        new Reward("Standard pack", 149, false, LocalDate.of(2024, Month.JULY, 1), "Couleur au choix"),
                        new Reward("Duo pack", 279, true, LocalDate.of(2024, Month.AUGUST, 5), "Deux sacs + accessoires")
                )
        );
        projects.add(ecoBackpack);

        var graphicAnthology = new Project(
                UUID.fromString("985a7685-3ff6-4d55-8cd6-1c7392e961a5"),
                "Graphic Novel Anthology",
                Category.PUBLISHING,
                "UK",
                "Bristol",
                24_000,
                21_650,
                LocalDate.of(2024, Month.JANUARY, 28),
                LocalDate.of(2024, Month.MARCH, 10),
                "Ink&Bones",
                Set.of("comics", "anthology", "fantasy"),
                List.of(
                        new Reward("Digital anthology", 14, false, LocalDate.of(2024, Month.MAY, 12), "PDF + wallpapers"),
                        new Reward("Hardcover", 32, false, LocalDate.of(2024, Month.JUNE, 30), "Livre relié"),
                        new Reward("Art print bundle", 55, true, LocalDate.of(2024, Month.JULY, 20), "Set de 5 prints")
                )
        );
        projects.add(graphicAnthology);

        var shortFilm = new Project(
                UUID.fromString("8f02cf67-8676-43f0-a974-8d9f29c5f1f0"),
                "Neo-Noir Short Film",
                Category.FILM,
                "US",
                "Chicago",
                55_000,
                61_780,
                LocalDate.of(2023, Month.DECEMBER, 1),
                LocalDate.of(2024, Month.JANUARY, 20),
                "Lensflare Studio",
                Set.of("cinema", "noir", "soundtrack"),
                List.of(
                        new Reward("Digital premiere", 20, false, LocalDate.of(2024, Month.MAY, 1), "Lien de streaming"),
                        new Reward("Behind the scenes", 45, false, LocalDate.of(2024, Month.MAY, 20), "Documentaire"),
                        new Reward("Set visit", 180, true, LocalDate.of(2024, Month.JUNE, 10), "Invitation sur le tournage")
                )
        );
        projects.add(shortFilm);

        var museumExperience = new Project(
                UUID.fromString("f63417d5-6630-4db1-a1a1-5f4c96d7b271"),
                "Interactive Museum Experience",
                Category.ART,
                "NL",
                "Amsterdam",
                75_000,
                88_420,
                LocalDate.of(2024, Month.FEBRUARY, 5),
                LocalDate.of(2024, Month.MARCH, 25),
                "Immersive Collective",
                Set.of("immersive", "education", "family"),
                List.of(
                        new Reward("Day pass", 22, false, LocalDate.of(2024, Month.SEPTEMBER, 1), "Entrée + audio guide"),
                        new Reward("Family pack", 60, false, LocalDate.of(2024, Month.SEPTEMBER, 10), "4 entrées"),
                        new Reward("Patron evening", 140, true, LocalDate.of(2024, Month.SEPTEMBER, 20), "Soirée privée")
                )
        );
        projects.add(museumExperience);

        PROJECTS = List.copyOf(projects);
        PROJECT_BY_ID = PROJECTS.stream().collect(toMap(Project::id, Function.identity()));

        BACKERS = List.of(
                new Backer(UUID.fromString("4b386082-24ab-4330-8cd5-55da743c2d78"), "Alicia Gomez", "ES", Set.of("design", "food", "travel"), 1_200, false),
                new Backer(UUID.fromString("fc8f1ed6-3795-4e5d-b8a3-680cf17d4f9f"), "Noah Patel", "CA", Set.of("games", "technology", "music"), 2_500, true),
                new Backer(UUID.fromString("1d6d6c39-d7f2-4d79-ae6d-7ae7b70bf3aa"), "Sofia Martins", "PT", Set.of("art", "film", "education"), 900, false),
                new Backer(UUID.fromString("7e6bba4f-83b4-4f76-b112-1a27636cbb38"), "Daniel Weber", "DE", Set.of("music", "design", "technology"), 1_800, true),
                new Backer(UUID.fromString("06df1c47-80e1-4603-83b7-7a2a664cc7cb"), "Chloe Martin", "FR", Set.of("food", "sustainability", "art"), 1_100, false),
                new Backer(UUID.fromString("6c6f9975-15b2-4a34-9d2b-47b28a1b3c53"), "Ethan Johnson", "US", Set.of("film", "photography", "design"), 3_600, true),
                new Backer(UUID.fromString("51a0fc39-76f9-4974-9f22-8688f050fde7"), "Mira Gupta", "IN", Set.of("technology", "education", "games"), 1_400, false)
        );
        BACKER_BY_ID = BACKERS.stream().collect(toMap(Backer::id, Function.identity()));

        PLEDGES = List.of(
                new Pledge(UUID.fromString("ba7dd5f4-2593-485b-bfb8-59b65513e4c2"), modularDesk.id(), BACKERS.get(0).id(), 99,
                        LocalDateTime.of(2023, Month.SEPTEMBER, 15, 9, 24), Optional.of(modularDesk.rewards().get(1))),
                new Pledge(UUID.fromString("f3b86a12-8500-4d5f-b95b-cf56cf0fe7af"), modularDesk.id(), BACKERS.get(5).id(), 279,
                        LocalDateTime.of(2023, Month.SEPTEMBER, 16, 16, 42), Optional.of(modularDesk.rewards().get(2))),
                new Pledge(UUID.fromString("f6e8a254-f7d2-47ab-9e6d-7b3c1ad89266"), ecoBackpack.id(), BACKERS.get(1).id(), 279,
                        LocalDateTime.of(2023, Month.OCTOBER, 3, 12, 10), Optional.of(ecoBackpack.rewards().get(2))),
                new Pledge(UUID.fromString("2b6b1ea6-708b-4ad4-98dd-5041c9e0b56d"), ecoBackpack.id(), BACKERS.get(3).id(), 149,
                        LocalDateTime.of(2023, Month.OCTOBER, 4, 8, 5), Optional.of(ecoBackpack.rewards().get(1))),
                new Pledge(UUID.fromString("785f256e-8f44-45cd-90da-89d4bde5a3c6"), ecoBackpack.id(), BACKERS.get(6).id(), 119,
                        LocalDateTime.of(2023, Month.OCTOBER, 6, 20, 18), Optional.of(ecoBackpack.rewards().get(0))),
                new Pledge(UUID.fromString("c8f4c2d4-b9cf-44a2-af59-2f6c621408e4"), indieAdventure.id(), BACKERS.get(1).id(), 150,
                        LocalDateTime.of(2024, Month.JANUARY, 9, 10, 45), Optional.of(indieAdventure.rewards().get(2))),
                new Pledge(UUID.fromString("0a2b6cdd-bb74-4a7f-9121-4a2d6f6ef9e0"), indieAdventure.id(), BACKERS.get(2).id(), 20,
                        LocalDateTime.of(2024, Month.JANUARY, 10, 14, 33), Optional.of(indieAdventure.rewards().get(0))),
                new Pledge(UUID.fromString("3a8bcf2e-d78a-4d72-a42f-c8f1b742f60e"), indieAdventure.id(), BACKERS.get(4).id(), 80,
                        LocalDateTime.of(2024, Month.JANUARY, 11, 21, 7), Optional.of(indieAdventure.rewards().get(1))),
                new Pledge(UUID.fromString("9f2d5fd0-63b6-4e5e-845e-8cc2a7c7670d"), fermentationKit.id(), BACKERS.get(4).id(), 39,
                        LocalDateTime.of(2023, Month.NOVEMBER, 6, 9, 2), Optional.of(fermentationKit.rewards().get(1))),
                new Pledge(UUID.fromString("64abf9ae-4341-494f-a501-ffcd04a17b1c"), fermentationKit.id(), BACKERS.get(0).id(), 95,
                        LocalDateTime.of(2023, Month.NOVEMBER, 7, 17, 20), Optional.of(fermentationKit.rewards().get(2))),
                new Pledge(UUID.fromString("4168ce02-29b1-47b9-a0db-7f4fe6a2416e"), fermentationKit.id(), BACKERS.get(6).id(), 12,
                        LocalDateTime.of(2023, Month.NOVEMBER, 8, 13, 55), Optional.of(fermentationKit.rewards().get(0))),
                new Pledge(UUID.fromString("889663e3-e7e7-48c2-bdd6-939b4fba8cb5"), synthwaveVinyl.id(), BACKERS.get(3).id(), 65,
                        LocalDateTime.of(2024, Month.FEBRUARY, 22, 11, 0), Optional.of(synthwaveVinyl.rewards().get(2))),
                new Pledge(UUID.fromString("f9c36a62-633c-46b0-b6fc-0ea8d1f5f55f"), synthwaveVinyl.id(), BACKERS.get(5).id(), 35,
                        LocalDateTime.of(2024, Month.FEBRUARY, 23, 18, 12), Optional.of(synthwaveVinyl.rewards().get(1))),
                new Pledge(UUID.fromString("0e215acd-542f-4f3f-8aa2-cc91f88eb8ba"), shortFilm.id(), BACKERS.get(2).id(), 45,
                        LocalDateTime.of(2023, Month.DECEMBER, 3, 19, 26), Optional.of(shortFilm.rewards().get(1))),
                new Pledge(UUID.fromString("3e45b272-701f-4cdf-90fb-f46fb4aa2b28"), shortFilm.id(), BACKERS.get(5).id(), 180,
                        LocalDateTime.of(2023, Month.DECEMBER, 4, 8, 41), Optional.of(shortFilm.rewards().get(2))),
                new Pledge(UUID.fromString("b6d0461f-314c-4b92-8e74-bae7c79e2d8f"), shortFilm.id(), BACKERS.get(0).id(), 20,
                        LocalDateTime.of(2023, Month.DECEMBER, 6, 22, 5), Optional.of(shortFilm.rewards().get(0))),
                new Pledge(UUID.fromString("d1c8120c-7a3f-438f-9d68-1f34b4ece2e1"), museumExperience.id(), BACKERS.get(6).id(), 140,
                        LocalDateTime.of(2024, Month.FEBRUARY, 7, 15, 12), Optional.of(museumExperience.rewards().get(2))),
                new Pledge(UUID.fromString("a7f56cf3-600f-4cb0-9bd2-5f4da112b34f"), museumExperience.id(), BACKERS.get(2).id(), 60,
                        LocalDateTime.of(2024, Month.FEBRUARY, 8, 10, 34), Optional.of(museumExperience.rewards().get(1))),
                new Pledge(UUID.fromString("ad4cf558-2d6a-4450-8019-b8dd64126618"), museumExperience.id(), BACKERS.get(1).id(), 22,
                        LocalDateTime.of(2024, Month.FEBRUARY, 9, 9, 15), Optional.of(museumExperience.rewards().get(0)))
        );
    }

    private KickstarterData() {
        throw new AssertionError("No instances");
    }

    /**
     * @return un flux réutilisable de projets.
     */
    public static Stream<Project> streamProjects() {
        return PROJECTS.stream();
    }

    /**
     * @return un flux réutilisable de backers.
     */
    public static Stream<Backer> streamBackers() {
        return BACKERS.stream();
    }

    /**
     * @return un flux réutilisable de promesses.
     */
    public static Stream<Pledge> streamPledges() {
        return PLEDGES.stream();
    }

    /**
     * Permet de récupérer un projet par son identifiant.
     */
    public static Project requireProject(UUID id) {
        return Objects.requireNonNull(PROJECT_BY_ID.get(id), () -> "Projet introuvable : " + id);
    }

    /**
     * Permet de récupérer un backer par son identifiant.
     */
    public static Backer requireBacker(UUID id) {
        return Objects.requireNonNull(BACKER_BY_ID.get(id), () -> "Backer introuvable : " + id);
    }

    /**
     * Utilitaire pratique pour internationaliser les pays sur deux lettres.
     */
    public static String countryDisplayName(String isoCode) {
        var locale = new Locale("", Objects.requireNonNull(isoCode, "isoCode"));
        return locale.getDisplayCountry(Locale.FRENCH);
    }
}
