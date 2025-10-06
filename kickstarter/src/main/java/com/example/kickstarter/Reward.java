package com.example.kickstarter;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Représente une récompense proposée par un projet Kickstarter.
 */
public record Reward(
        String title,
        double minimumPledge,
        boolean limited,
        LocalDate estimatedDelivery,
        String description
) {
    public Reward {
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(estimatedDelivery, "estimatedDelivery");
        Objects.requireNonNull(description, "description");
        if (minimumPledge < 0) {
            throw new IllegalArgumentException("minimumPledge doit être positif");
        }
    }
}
