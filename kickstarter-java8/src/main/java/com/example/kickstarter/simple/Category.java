package com.example.kickstarter.simple;

/**
 * Catégories simplifiées pour les projets Kickstarter.
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

    public String getDisplayName() {
        return displayName;
    }
}
