package com.example.patterns.structural.composite;

public final class MenuItem implements MenuComponent {
    private final String label;

    public MenuItem(String label) {
        this.label = label;
    }

    @Override
    public String render() {
        return "- " + label;
    }
}
