package com.example.patterns.structural.bridge;

public class Circle {
    private final Renderer renderer;
    private float radius;

    public Circle(Renderer renderer, float radius) {
        this.renderer = renderer;
        this.radius = radius;
    }

    public String draw() {
        return renderer.renderCircle(radius);
    }
}
