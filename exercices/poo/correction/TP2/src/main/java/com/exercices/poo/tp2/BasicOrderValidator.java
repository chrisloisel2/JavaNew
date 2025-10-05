package com.exercices.poo.tp2;

import java.util.Objects;

public final class BasicOrderValidator implements OrderValidator {
    @Override
    public void validate(Order order) {
        Objects.requireNonNull(order);
        if (order.customerEmail() == null || !order.customerEmail().contains("@")) {
            throw new IllegalArgumentException("Email client invalide");
        }
        if (order.items().isEmpty()) {
            throw new IllegalArgumentException("La commande doit contenir des articles");
        }
    }
}
