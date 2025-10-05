package com.exercices.poo.tp2;

/**
 * Exemple de code legacy concentrant trop de responsabilités.
 *
 * Violations SOLID :
 * - SRP : création, validation, paiement et expédition sont dans la même classe.
 * - OCP : impossible de changer un moyen de paiement sans modifier la classe.
 * - LSP : aucune abstraction claire pour substituer un moyen de paiement.
 * - ISP : les clients devraient dépendre d'opérations dont ils n'ont pas besoin.
 * - DIP : dépend directement de classes concrètes (paiement, expédition).
 */
public final class OrderProcessorLegacy {

    private OrderProcessorLegacy() {
    }

    public void process(Order order) {
        throw new UnsupportedOperationException("Legacy non maintenable");
    }
}
