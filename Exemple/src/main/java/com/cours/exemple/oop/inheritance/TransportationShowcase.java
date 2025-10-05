package com.cours.exemple.oop.inheritance;

import java.util.List;

/**
 * Illustre l'héritage via une hiérarchie de moyens de transport.
 */
public final class TransportationShowcase {

    private TransportationShowcase() {
    }

    public static void example() {
        System.out.println("[Héritage] Parc de transports urbains");

        List<Vehicle> fleet = List.of(
                new Bus("Ligne 12", 42),
                new Tram("T2", true),
                new ElectricScooter("FreeRide"));

        for (Vehicle vehicle : fleet) {
            vehicle.startService();
        }
    }

    private abstract static class Vehicle {
        private final String name;

        Vehicle(String name) {
            this.name = name;
        }

        void startService() {
            System.out.printf("%s démarre. %s\n", name, status());
        }

        abstract String status();
    }

    private static final class Bus extends Vehicle {
        private final int seats;

        Bus(String name, int seats) {
            super(name);
            this.seats = seats;
        }

        @Override
        String status() {
            return "Capacité " + seats + " passagers";
        }
    }

    private static final class Tram extends Vehicle {
        private final boolean hasPriorityLane;

        Tram(String name, boolean hasPriorityLane) {
            super(name);
            this.hasPriorityLane = hasPriorityLane;
        }

        @Override
        String status() {
            return hasPriorityLane ? "Circule sur voie prioritaire" : "Circule en voie partagée";
        }
    }

    private static final class ElectricScooter extends Vehicle {

        ElectricScooter(String name) {
            super(name);
        }

        @Override
        String status() {
            return "Batterie chargée à 100%";
        }
    }
}
