package com.example.patterns.structural.flyweight;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IconFactory {

    private final Map<String, Icon> cache = new ConcurrentHashMap<>();

    public Icon getIcon(String name) {
        return cache.computeIfAbsent(name, Icon::new);
    }

    public record Icon(String name) { }
}
