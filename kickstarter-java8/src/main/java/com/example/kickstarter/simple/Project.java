package com.example.kickstarter.simple;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Projet Kickstarter en version Java 8.
 */
public final class Project {

    private final UUID id;
    private final String title;
    private final Category category;
    private final String country;
    private final String city;
    private final double goalAmount;
    private final double pledgedAmount;
    private final LocalDate launchDate;
    private final LocalDate deadline;
    private final String owner;
    private final Set<String> tags;
    private final List<Reward> rewards;

    public Project(UUID id,
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
                   List<Reward> rewards) {
        this.id = Objects.requireNonNull(id, "id");
        this.title = Objects.requireNonNull(title, "title");
        this.category = Objects.requireNonNull(category, "category");
        this.country = Objects.requireNonNull(country, "country");
        this.city = Objects.requireNonNull(city, "city");
        if (goalAmount <= 0) {
            throw new IllegalArgumentException("goalAmount doit être strictement positif");
        }
        this.goalAmount = goalAmount;
        if (pledgedAmount < 0) {
            throw new IllegalArgumentException("pledgedAmount ne peut pas être négatif");
        }
        this.pledgedAmount = pledgedAmount;
        this.launchDate = Objects.requireNonNull(launchDate, "launchDate");
        this.deadline = Objects.requireNonNull(deadline, "deadline");
        this.owner = Objects.requireNonNull(owner, "owner");
        Set<String> tagsCopy = new LinkedHashSet<>(Objects.requireNonNull(tags, "tags"));
        this.tags = Collections.unmodifiableSet(tagsCopy);
        List<Reward> rewardCopy = new ArrayList<>(Objects.requireNonNull(rewards, "rewards"));
        this.rewards = Collections.unmodifiableList(rewardCopy);
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Category getCategory() {
        return category;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public double getGoalAmount() {
        return goalAmount;
    }

    public double getPledgedAmount() {
        return pledgedAmount;
    }

    public LocalDate getLaunchDate() {
        return launchDate;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public String getOwner() {
        return owner;
    }

    public Set<String> getTags() {
        return tags;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public double fundingProgress() {
        return pledgedAmount / goalAmount * 100.0;
    }

    public boolean isFinished(LocalDate referenceDate) {
        Objects.requireNonNull(referenceDate, "referenceDate");
        return !deadline.isAfter(referenceDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        Project project = (Project) o;
        return Double.compare(project.goalAmount, goalAmount) == 0
                && Double.compare(project.pledgedAmount, pledgedAmount) == 0
                && id.equals(project.id)
                && title.equals(project.title)
                && category == project.category
                && country.equals(project.country)
                && city.equals(project.city)
                && launchDate.equals(project.launchDate)
                && deadline.equals(project.deadline)
                && owner.equals(project.owner)
                && tags.equals(project.tags)
                && rewards.equals(project.rewards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, category, country, city, goalAmount, pledgedAmount, launchDate, deadline, owner, tags, rewards);
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category=" + category +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", goalAmount=" + goalAmount +
                ", pledgedAmount=" + pledgedAmount +
                ", launchDate=" + launchDate +
                ", deadline=" + deadline +
                ", owner='" + owner + '\'' +
                ", tags=" + tags +
                ", rewards=" + rewards +
                '}';
    }
}
