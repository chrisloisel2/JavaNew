package com.example.kickstarter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Contribution d'un backer à un projet.
 */
public record Pledge(
        UUID id,
        UUID projectId,
        UUID backerId,
        double amount,
        LocalDateTime pledgedAt,
        Optional<Reward> reward
) {
    public Pledge {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(projectId, "projectId");
        Objects.requireNonNull(backerId, "backerId");
        Objects.requireNonNull(pledgedAt, "pledgedAt");
        reward = Objects.requireNonNullElse(reward, Optional.empty());
        if (amount <= 0) {
            throw new IllegalArgumentException("amount doit être strictement positif");
        }
    }
}
