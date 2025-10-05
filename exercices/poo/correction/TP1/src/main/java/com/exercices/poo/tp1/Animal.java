package com.exercices.poo.tp1;

/**
 * Classe de base pour tous les animaux de l'animalerie.
 */
public abstract class Animal {

    private final String nom;
    private int age;
    private double poids;

    protected Animal(String nom, int age, double poids) {
        this.nom = nom;
        this.age = age;
        this.poids = poids;
    }

    public String getNom() {
        return nom;
    }

    protected int getAge() {
        return age;
    }

    protected double getPoids() {
        return poids;
    }

    protected void setPoids(double poids) {
        this.poids = poids;
    }

    public final void vieillir() {
        age++;
    }

    public abstract String makeSound();

    public String description() {
        return "%s (%d ans, %.1f kg)".formatted(nom, age, poids);
    }
}
