package com.example.kickstarter.simple;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Représente un contributeur de projets Kickstarter (version Java 8 sans record).
 */
public final class Backer {

    private final UUID id;
    private final String name;
    private final String country;
    private final Set<String> interests;
    private final double annualBudget;
    private final boolean professional;

    public Backer(UUID id,
                  String name,
                  String country,
                  Set<String> interests,
                  double annualBudget,
                  boolean professional) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.country = Objects.requireNonNull(country, "country");
        Set<String> copy = new LinkedHashSet<>(Objects.requireNonNull(interests, "interests"));
        this.interests = Collections.unmodifiableSet(copy);
        if (annualBudget < 0) {
            throw new IllegalArgumentException("annualBudget doit être positif");
        }
        this.annualBudget = annualBudget;
        this.professional = professional;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public Set<String> getInterests() {
        return interests;
    }

    public double getAnnualBudget() {
        return annualBudget;
    }

    public boolean isProfessional() {
        return professional;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Backer)) {
            return false;
        }
        Backer backer = (Backer) o;
        return professional == backer.professional
                && Double.compare(backer.annualBudget, annualBudget) == 0
                && id.equals(backer.id)
                && name.equals(backer.name)
                && country.equals(backer.country)
                && interests.equals(backer.interests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, country, interests, annualBudget, professional);
    }

    @Override
    public String toString() {
        return "Backer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", interests=" + interests +
                ", annualBudget=" + annualBudget +
                ", professional=" + professional +
                '}';
    }
}
