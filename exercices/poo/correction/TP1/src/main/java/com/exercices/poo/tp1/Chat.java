package com.exercices.poo.tp1;

/**
 * Implémentation concrète d'un chat.
 */
public final class Chat extends Animal {

    public Chat(String nom, int age, double poids) {
        super(nom, age, poids);
    }

    @Override
    public String makeSound() {
        return "Miaou";
    }

    public String faireSesGriffes() {
        return "%s fait ses griffes".formatted(getNom());
    }
}
