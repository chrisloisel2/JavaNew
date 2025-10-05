package com.exercices.fondamentaux.tp2;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class DemoApp {

    private DemoApp() {
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.FRANCE);

        String rawText = "   Java est un langage moderne. Java permet d'écrire du code moderne et robuste !   ";
        System.out.println("Texte brut : " + rawText);

        String cleaned = TextCleaner.trim(rawText);
        String title = TextCleaner.toTitleCase(rawText);
        String withoutAccents = TextCleaner.removeAccents("Éléphant à l'orée" );

        System.out.printf("Nettoyé : '%s'%nTitre : '%s'%nSans accents : '%s'%n", cleaned, title, withoutAccents);

        Set<String> ignored = Set.of("un", "et", "du");
        Map<String, Integer> basicCount = TextAnalyzer.countWords(cleaned);
        Map<String, Integer> filteredCount = TextAnalyzer.countWords(cleaned, ignored);

        System.out.println("\nComptage brut :");
        TextAnalyzer.printReport(basicCount);

        System.out.println("\nComptage filtré :");
        TextAnalyzer.printReport(filteredCount);

        runAssertions(cleaned, withoutAccents, filteredCount);
    }

    private static void runAssertions(String cleaned, String withoutAccents, Map<String, Integer> filteredCount) {
        Objects.requireNonNull(cleaned);
        Objects.requireNonNull(withoutAccents);
        Objects.requireNonNull(filteredCount);

        assert cleaned.startsWith("Java") : "Le texte nettoyé doit débuter par Java";
        assert withoutAccents.equals("Elephant a l'oree") : "Les accents doivent être retirés";
        assert filteredCount.getOrDefault("java", 0) >= 2 : "Java doit être compté au moins deux fois";
    }
}
