package com.example.java12to16.patterns;

/**
 * Exemple de hiérarchie scellée (preview en Java 15/16, finalisée en Java 17).
 */
public sealed interface Shape permits Shape.Circle, Shape.Rectangle {

    double area();

    record Circle(double radius) implements Shape {
        @Override
        public double area() {
            return Math.PI * radius * radius;
        }
    }

    record Rectangle(double width, double height) implements Shape {
        @Override
        public double area() {
            return width * height;
        }
    }
}
