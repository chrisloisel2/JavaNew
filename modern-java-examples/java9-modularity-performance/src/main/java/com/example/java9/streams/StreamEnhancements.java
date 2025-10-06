package com.example.java9.streams;

import java.util.List;
import java.util.stream.Stream;

/**
 * Démonstration des nouvelles opérations introduites sur {@link Stream} en Java 9.
 */
public final class StreamEnhancements {

    private StreamEnhancements() {
        // utilitaire
    }

    /**
     * Sélectionne les nombres jusqu'à ce qu'une valeur paire soit rencontrée.
     */
    public static List<Integer> takeWhileExample(Stream<Integer> source) {
        return source.takeWhile(n -> n % 2 != 0).toList();
    }

    /**
     * Ignore les nombres jusqu'à ce qu'une valeur paire soit rencontrée.
     */
    public static List<Integer> dropWhileExample(Stream<Integer> source) {
        return source.dropWhile(n -> n % 2 != 0).limit(5).toList();
    }

    /**
     * Crée un flux infini en utilisant la nouvelle signature d'iterate avec prédicat d'arrêt.
     */
    public static List<Integer> iterateExample(int startInclusive, int endExclusive) {
        return Stream.iterate(startInclusive, n -> n < endExclusive, n -> n + 3).toList();
    }
}
