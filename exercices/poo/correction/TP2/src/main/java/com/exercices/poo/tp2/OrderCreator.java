package com.exercices.poo.tp2;

public interface OrderCreator {
    Order create(String customerEmail, String... items);
}
