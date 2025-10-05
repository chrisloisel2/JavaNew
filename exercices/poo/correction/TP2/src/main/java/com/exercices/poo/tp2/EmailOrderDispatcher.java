package com.exercices.poo.tp2;

import java.util.Locale;

public final class EmailOrderDispatcher implements OrderDispatcher {
    @Override
    public void dispatch(Order order) {
        System.out.printf(Locale.FRANCE, "Envoi de l'email à %s pour la commande %s%n", order.customerEmail(), order.id());
    }
}
