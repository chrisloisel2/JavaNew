package com.exercices.poo.tp2;

import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public final class DefaultOrderCreator implements OrderCreator {
    @Override
    public Order create(String customerEmail, String... items) {
        Objects.requireNonNull(customerEmail);
        if (items == null || items.length == 0) {
            throw new IllegalArgumentException("Une commande doit contenir au moins un article");
        }
        return new Order(UUID.randomUUID().toString(), customerEmail, Arrays.stream(items).toList(), Instant.now());
    }
}
