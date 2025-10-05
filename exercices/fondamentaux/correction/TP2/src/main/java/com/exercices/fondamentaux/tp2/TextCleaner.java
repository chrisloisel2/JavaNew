package com.exercices.fondamentaux.tp2;

import java.text.Normalizer;
import java.util.Objects;

/**
 * Méthodes utilitaires pour nettoyer et normaliser des chaînes de caractères.
 */
public final class TextCleaner {

    private TextCleaner() {
    }

    /**
     * Supprime les espaces superflus en début et fin de chaîne.
     *
     * @param input texte d'origine
     * @return texte sans espaces superflus
     */
    public static String trim(String input) {
        return Objects.requireNonNull(input, "input").trim();
    }

    /**
     * Met la chaîne au format Titre (première lettre en majuscule pour chaque mot).
     *
     * @param input texte d'origine
     * @return texte formaté
     */
    public static String toTitleCase(String input) {
        String trimmed = trim(input).toLowerCase();
        StringBuilder builder = new StringBuilder(trimmed.length());
        boolean capitalize = true;
        for (char c : trimmed.toCharArray()) {
            if (Character.isWhitespace(c) || c == '-') {
                builder.append(c);
                capitalize = true;
            } else if (capitalize) {
                builder.append(Character.toTitleCase(c));
                capitalize = false;
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    /**
     * Retire les accents et autres diacritiques.
     *
     * @param input texte d'origine
     * @return texte sans accents
     */
    public static String removeAccents(String input) {
        String normalized = Normalizer.normalize(Objects.requireNonNull(input, "input"), Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
