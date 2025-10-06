package com.example.java12to16.patterns;

/**
 * Record pour déclarer une classe immuable de manière concise.
 */
public record Point(int x, int y) {
    public int manhattanDistance() {
        return Math.abs(x) + Math.abs(y);
    }
}
