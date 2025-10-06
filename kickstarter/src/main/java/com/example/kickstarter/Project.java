package com.example.kickstarter;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Projet Kickstarter avec des informations enrichies pour travailler sur les streams.
 */
public record Project(
        UUID id,
        String title,
        Category category,
        String country,
        String city,
        double goalAmount,
        double pledgedAmount,
        LocalDate launchDate,
        LocalDate deadline,
        String owner,
        Set<String> tags,
        List<Reward> rewards
) {
    public Project {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(category, "category");
        Objects.requireNonNull(country, "country");
        Objects.requireNonNull(city, "city");
        Objects.requireNonNull(launchDate, "launchDate");
        Objects.requireNonNull(deadline, "deadline");
        Objects.requireNonNull(owner, "owner");
        tags = Set.copyOf(Objects.requireNonNull(tags, "tags"));
        rewards = List.copyOf(Objects.requireNonNull(rewards, "rewards"));
        if (goalAmount <= 0) {
            throw new IllegalArgumentException("goalAmount doit être strictement positif");
        }
        if (pledgedAmount < 0) {
            throw new IllegalArgumentException("pledgedAmount ne peut pas être négatif");
        }
    }

    /**
     * @return pourcentage de financement (100 = objectif atteint).
     */
    public double fundingProgress() {
        return pledgedAmount / goalAmount * 100.0;
    }

    /**
     * @return true si la campagne est terminée (deadline passée).
     */
    public boolean isFinished(LocalDate referenceDate) {
        return deadline.isBefore(Objects.requireNonNull(referenceDate, "referenceDate"))
                || deadline.isEqual(referenceDate);
    }
}
