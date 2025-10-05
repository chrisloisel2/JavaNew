package com.cours.exemple.basics.variables;

/**
 * Démonstration de la déclaration de variables et de l'utilisation des types primitifs et objets.
 */
public final class CustomerProfile {
    private static final double TVA_STANDARD = 0.2; // constante partagée

    private CustomerProfile() {
    }

    public static void example() {
        System.out.println("[Variables] Création d'un profil client");

        String firstName = "Lina"; // type référence
        String lastName = "Nguyen";
        int age = 29; // type primitif
        boolean premiumMember = true;
        double monthlySpending = 320.75;
        char gender = 'F';

        double yearlySpending = monthlySpending * 12; // utilisation d'opérateurs
        double yearlyTaxes = yearlySpending * TVA_STANDARD;

        System.out.printf("Client: %s %s (%c)\n", firstName, lastName, gender);
        System.out.printf("Âge: %d ans - Premium: %b\n", age, premiumMember);
        System.out.printf("Dépenses annuelles: %.2f € dont %.2f € de TVA\n", yearlySpending, yearlyTaxes);
    }
}
