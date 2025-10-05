package com.cours.exemple.oop.interfaces;

import java.util.List;

/**
 * Utilisation d'interfaces pour abstraire différents canaux de notification.
 */
public final class NotificationDemo {

    private NotificationDemo() {
    }

    public static void example() {
        System.out.println("[Interfaces] Notifications multi-canaux");

        List<Notifier> notifiers = List.of(new EmailNotifier(), new SmsNotifier(), new PushNotifier());
        for (Notifier notifier : notifiers) {
            notifier.send("Commande expédiée", "Votre colis est en route.");
        }
    }

    private interface Notifier {
        void send(String title, String message);
    }

    private static final class EmailNotifier implements Notifier {
        @Override
        public void send(String title, String message) {
            System.out.printf("Email => %s : %s\n", title, message);
        }
    }

    private static final class SmsNotifier implements Notifier {
        @Override
        public void send(String title, String message) {
            System.out.printf("SMS => %s\n", message);
        }
    }

    private static final class PushNotifier implements Notifier {
        @Override
        public void send(String title, String message) {
            System.out.printf("Push => %s | %s\n", title, message);
        }
    }
}
