package com.cours.exemple.basics.strings;

/**
 * Manipulation d'objets String : concaténation, transformation et formatage.
 */
public final class GreetingComposer {

    private GreetingComposer() {
    }

    public static void example() {
        System.out.println("[Chaînes] Génération d'un message personnalisé");

        String firstName = "Amadou";
        String lastName = "Diallo";
        String company = "TerraMarket";

        String fullName = firstName + " " + lastName.toUpperCase();
        String message = String.format("Bonjour %s, bienvenue chez %s !", fullName, company);
        String excerpt = message.substring(0, Math.min(25, message.length()));

        System.out.println(message);
        System.out.println("Extrait du message: " + excerpt + "...");
        System.out.println("Nombre de caractères: " + message.length());
    }
}
