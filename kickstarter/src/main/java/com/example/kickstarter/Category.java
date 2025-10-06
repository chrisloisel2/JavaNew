package com.example.kickstarter;

/**
 * Enumeration des principales catégories de projets présentes dans l'échantillon.
 */
public enum Category {
    ART("Art"),
    DESIGN("Design"),
    FILM("Film & Video"),
    FOOD("Food"),
    GAMES("Games"),
    MUSIC("Music"),
    PUBLISHING("Publishing"),
    TECHNOLOGY("Technology");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return nom "marketing" lisible de la catégorie.
     */
    public String displayName() {
        return displayName;
    }
}
