package com.example.patterns.structural.bridge;

public class VectorRenderer implements Renderer {
    @Override
    public String renderCircle(float radius) {
        return "Vector circle of radius " + radius;
    }
}
