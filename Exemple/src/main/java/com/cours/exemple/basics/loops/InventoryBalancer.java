package com.cours.exemple.basics.loops;

import java.util.Arrays;

/**
 * Utilisation des boucles for et while pour répartir un stock de produits.
 */
public final class InventoryBalancer {

    private InventoryBalancer() {
    }

    public static void example() {
        System.out.println("[Boucles] Répartition d'un stock entre entrepôts");

        int[] warehouses = {50, 35, 10};
        int incomingStock = 40;

        for (int i = 0; i < warehouses.length; i++) {
            int capacity = 60;
            while (warehouses[i] < capacity && incomingStock > 0) {
                warehouses[i]++;
                incomingStock--;
            }
            System.out.printf("Entrepôt %d: %d unités\n", i + 1, warehouses[i]);
        }

        System.out.printf("Stock restant en transit: %d unités\n", incomingStock);
        System.out.println("Répartition finale: " + Arrays.toString(warehouses));
    }
}
