package com.exercices.poo.tp1;

/**
 * Implémentation concrète d'un chien.
 */
public final class Chien extends Animal {

    public Chien(String nom, int age, double poids) {
        super(nom, age, poids);
    }

    @Override
    public String makeSound() {
        return "Wouf !";
    }

    public String rapporterObjet(String objet) {
        return "%s rapporte %s".formatted(getNom(), objet);
    }
}
