package com.example.java21;

/**
 * Record et pattern matching pour switch.
 */
public sealed interface Geometry permits Geometry.Point, Geometry.Line {

    record Point(int x, int y) implements Geometry { }

    record Line(Point start, Point end) implements Geometry { }

    static String describe(Geometry geometry) {
        return switch (geometry) {
            case Point(int x, int y) -> "Point(%d,%d)".formatted(x, y);
            case Line(Point(var x1, var y1), Point(var x2, var y2)) ->
                    "Ligne de (%d,%d) à (%d,%d)".formatted(x1, y1, x2, y2);
        };
    }
}
