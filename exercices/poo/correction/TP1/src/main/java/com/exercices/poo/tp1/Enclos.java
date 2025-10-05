package com.exercices.poo.tp1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Gestionnaire d'enclos encapsulant la liste d'animaux.
 */
public final class Enclos {

    private final String nom;
    private final List<Animal> animaux = new ArrayList<>();

    public Enclos(String nom) {
        this.nom = nom;
    }

    public void ajouter(Animal animal) {
        animaux.add(animal);
    }

    public boolean retirer(Animal animal) {
        return animaux.remove(animal);
    }

    public void nourrirTous() {
        animaux.forEach(animal -> animal.setPoids(animal.getPoids() + 0.1));
    }

    public List<Animal> listerAnimaux() {
        return Collections.unmodifiableList(animaux);
    }

    @Override
    public String toString() {
        return "Enclos %s (%d animaux)".formatted(nom, animaux.size());
    }
}
