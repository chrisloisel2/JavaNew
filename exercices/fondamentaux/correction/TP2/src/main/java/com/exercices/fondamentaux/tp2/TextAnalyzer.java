package com.exercices.fondamentaux.tp2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Classe fournissant des statistiques simples sur un texte.
 */
public final class TextAnalyzer {

    private TextAnalyzer() {
    }

    public static Map<String, Integer> countWords(String text) {
        return countWords(text, Collections.emptySet());
    }

    public static Map<String, Integer> countWords(String text, Set<String> ignoredWords) {
        Objects.requireNonNull(text, "text");
        Objects.requireNonNull(ignoredWords, "ignoredWords");

        List<String> words = Arrays.stream(text.split("\\W+"))
                .map(String::toLowerCase)
                .map(TextCleaner::trim)
                .filter(word -> !word.isBlank())
                .filter(word -> !ignoredWords.contains(word))
                .toList();

        return words.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(w -> 1)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, java.util.LinkedHashMap::new));
    }

    public static void printReport(Map<String, Integer> counts) {
        counts.forEach((word, occurrences) -> System.out.printf(Locale.FRANCE, "%s -> %d%n", word, occurrences));
    }
}
