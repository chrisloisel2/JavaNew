package com.example.java12to16.patterns;

/**
 * Text blocks pour écrire des chaînes multilignes lisibles.
 */
public class TextBlockExamples {

    public String jsonPayload(String name) {
        return """
                {
                  "name": "%s"
                }
                """.formatted(name);
    }
}
