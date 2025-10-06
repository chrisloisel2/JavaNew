package com.example.java12to16.patterns;

/**
 * Pattern matching pour {@code instanceof} introduit en Java 14 (preview) et stabilisé ensuite.
 */
public class PatternMatchingDemo {

    public String uppercaseIfString(Object obj) {
        if (obj instanceof String s && !s.isBlank()) {
            return s.toUpperCase();
        }
        return "";
    }
}
