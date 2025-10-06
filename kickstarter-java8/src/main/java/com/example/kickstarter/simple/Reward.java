package com.example.kickstarter.simple;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Niveau de récompense proposé par un projet Kickstarter.
 */
public final class Reward {

    private final String title;
    private final double minimumPledge;
    private final boolean limited;
    private final LocalDate estimatedDelivery;
    private final String description;

    public Reward(String title,
                  double minimumPledge,
                  boolean limited,
                  LocalDate estimatedDelivery,
                  String description) {
        this.title = Objects.requireNonNull(title, "title");
        if (minimumPledge < 0) {
            throw new IllegalArgumentException("minimumPledge doit être positif");
        }
        this.minimumPledge = minimumPledge;
        this.limited = limited;
        this.estimatedDelivery = Objects.requireNonNull(estimatedDelivery, "estimatedDelivery");
        this.description = Objects.requireNonNull(description, "description");
    }

    public String getTitle() {
        return title;
    }

    public double getMinimumPledge() {
        return minimumPledge;
    }

    public boolean isLimited() {
        return limited;
    }

    public LocalDate getEstimatedDelivery() {
        return estimatedDelivery;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reward)) {
            return false;
        }
        Reward reward = (Reward) o;
        return limited == reward.limited
                && Double.compare(reward.minimumPledge, minimumPledge) == 0
                && title.equals(reward.title)
                && estimatedDelivery.equals(reward.estimatedDelivery)
                && description.equals(reward.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, minimumPledge, limited, estimatedDelivery, description);
    }

    @Override
    public String toString() {
        return "Reward{" +
                "title='" + title + '\'' +
                ", minimumPledge=" + minimumPledge +
                ", limited=" + limited +
                ", estimatedDelivery=" + estimatedDelivery +
                ", description='" + description + '\'' +
                '}';
    }
}
