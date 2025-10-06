package com.example.java11;

import java.util.List;

/**
 * Montre les nouvelles méthodes utilitaires sur {@link String}.
 */
public class StringEnhancements {

    public boolean isBlank(String input) {
        return input.isBlank();
    }

    public List<String> splitLines(String input) {
        return input.lines().map(String::strip).toList();
    }

    public String normalizeAndRepeat(String input, int times) {
        return input.strip().repeat(times);
    }
}
