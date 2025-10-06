package com.example.patterns.behavioral.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NewsAgency {

    private final List<Consumer<String>> subscribers = new ArrayList<>();

    public void subscribe(Consumer<String> listener) {
        subscribers.add(listener);
    }

    public void publish(String headline) {
        subscribers.forEach(listener -> listener.accept(headline));
    }
}
