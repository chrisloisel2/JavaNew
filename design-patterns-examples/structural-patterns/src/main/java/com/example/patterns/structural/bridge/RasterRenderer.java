package com.example.patterns.structural.bridge;

public class RasterRenderer implements Renderer {
    @Override
    public String renderCircle(float radius) {
        return "Rasterizing circle with radius " + radius;
    }
}
