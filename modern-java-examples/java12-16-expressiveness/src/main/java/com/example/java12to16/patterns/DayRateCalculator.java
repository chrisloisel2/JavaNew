package com.example.java12to16.patterns;

/**
 * Switch expressions avec la nouvelle syntaxe fléchée.
 */
public class DayRateCalculator {

    public int openingHours(Day day) {
        return switch (day) {
            case MONDAY, FRIDAY -> 6;
            case SATURDAY -> 4;
            case SUNDAY -> 2;
            default -> 0;
        };
    }

    public enum Day { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }
}
