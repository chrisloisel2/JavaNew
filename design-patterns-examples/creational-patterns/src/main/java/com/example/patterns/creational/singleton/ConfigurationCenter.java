package com.example.patterns.creational.singleton;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Singleton thread-safe basé sur un holder statique.
 */
public final class ConfigurationCenter {

    private static final AtomicReference<ConfigurationCenter> INSTANCE = new AtomicReference<>();

    private ConfigurationCenter() {
    }

    public static ConfigurationCenter getInstance() {
        INSTANCE.compareAndSet(null, new ConfigurationCenter());
        return INSTANCE.get();
    }

    public String get(String key) {
        return switch (key) {
            case "theme" -> "dark";
            default -> "default";
        };
    }
}
