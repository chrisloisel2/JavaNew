package com.example.kickstarter.simple;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Contribution d'un backer à un projet.
 */
public final class Pledge {

    private final UUID id;
    private final UUID projectId;
    private final UUID backerId;
    private final double amount;
    private final LocalDateTime pledgedAt;
    private final Optional<Reward> reward;

    public Pledge(UUID id,
                  UUID projectId,
                  UUID backerId,
                  double amount,
                  LocalDateTime pledgedAt,
                  Optional<Reward> reward) {
        this.id = Objects.requireNonNull(id, "id");
        this.projectId = Objects.requireNonNull(projectId, "projectId");
        this.backerId = Objects.requireNonNull(backerId, "backerId");
        if (amount <= 0) {
            throw new IllegalArgumentException("amount doit être strictement positif");
        }
        this.amount = amount;
        this.pledgedAt = Objects.requireNonNull(pledgedAt, "pledgedAt");
        this.reward = Objects.requireNonNull(reward, "reward");
    }

    public UUID getId() {
        return id;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public UUID getBackerId() {
        return backerId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getPledgedAt() {
        return pledgedAt;
    }

    public Optional<Reward> getReward() {
        return reward;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pledge)) {
            return false;
        }
        Pledge pledge = (Pledge) o;
        return Double.compare(pledge.amount, amount) == 0
                && id.equals(pledge.id)
                && projectId.equals(pledge.projectId)
                && backerId.equals(pledge.backerId)
                && pledgedAt.equals(pledge.pledgedAt)
                && reward.equals(pledge.reward);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectId, backerId, amount, pledgedAt, reward);
    }

    @Override
    public String toString() {
        return "Pledge{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", backerId=" + backerId +
                ", amount=" + amount +
                ", pledgedAt=" + pledgedAt +
                ", reward=" + reward +
                '}';
    }
}
