package com.example.kickstarter;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Représente un contributeur de projets Kickstarter.
 */
public record Backer(
        UUID id,
        String name,
        String country,
        Set<String> interests,
        double annualBudget,
        boolean professional
) {
    public Backer {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(country, "country");
        interests = Set.copyOf(Objects.requireNonNull(interests, "interests"));
        if (annualBudget < 0) {
            throw new IllegalArgumentException("annualBudget doit être positif");
        }
    }
}
