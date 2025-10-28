package com.example.java9.streams;

import java.util.List;
import java.util.stream.Stream;


public final class StreamEnhancements {

    private StreamEnhancements() {
    }


    public static List<Integer> takeWhileExample(Stream<Integer> source) {
        return source.takeWhile(n -> n % 2 != 0).toList();
    }

    public static List<Integer> dropWhileExample(Stream<Integer> source) {
        return source.dropWhile(n -> n % 2 != 0).limit(5).toList();
    }

    public static List<Integer> iterateExample(int startInclusive, int endExclusive) {
        return Stream.iterate(startInclusive, n -> n < endExclusive, n -> n + 3).toList();
    }
}
