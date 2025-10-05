package com.exercices.poo.tp1;

import java.util.List;

public final class AnimalerieApp {

    private AnimalerieApp() {
    }

    public static void main(String[] args) {
        Enclos enclosChiens = new Enclos("Chiens");
        Enclos enclosChats = new Enclos("Chats");

        Chien rex = new Chien("Rex", 3, 18.5);
        Chat chipie = new Chat("Chipie", 2, 4.2);
        Chat simba = new Chat("Simba", 4, 5.1);

        enclosChiens.ajouter(rex);
        enclosChats.ajouter(chipie);
        enclosChats.ajouter(simba);

        enclosChiens.nourrirTous();
        enclosChats.nourrirTous();

        List<Animal> tous = List.of(rex, chipie, simba);
        for (Animal animal : tous) {
            String description = switch (animal) {
                case Chien chien -> chien.description() + " - " + chien.rapporterObjet("balle");
                case Chat chat -> chat.description() + " - " + chat.faireSesGriffes();
                default -> animal.description();
            };
            System.out.printf("%s : %s%n", animal.makeSound(), description);
        }

        System.out.println(enclosChiens);
        enclosChiens.listerAnimaux().forEach(animal -> System.out.println(" - " + animal.description()));
    }
}
