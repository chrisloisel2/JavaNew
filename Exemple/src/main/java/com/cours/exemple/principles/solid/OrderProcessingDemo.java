package com.cours.exemple.principles.solid;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Démonstration guidée des principes SOLID autour d'un traitement de commande.
 */
public final class OrderProcessingDemo {

    private OrderProcessingDemo() {
    }

    public static void example() {
        System.out.println("[SOLID] Traitement d'une commande e-commerce");

        Order order = new Order("CMD-2024-001", BigDecimal.valueOf(189.90));
        List<OrderValidator> validators = List.of(new AmountValidator(), new FraudCheckValidator());
        OrderRepository repository = new InMemoryOrderRepository();
        NotificationGateway notificationGateway = new ConsoleNotificationGateway();

        OrderProcessor processor = new OrderProcessor(validators, repository, notificationGateway);
        processor.process(order);
    }

    /**
     * Entité représentant une commande minimaliste.
     */
    private static final class Order {
        private final String reference;
        private final BigDecimal total;
        private Status status = Status.DRAFT;
        private LocalDateTime processedAt;

        Order(String reference, BigDecimal total) {
            this.reference = reference;
            this.total = total;
        }

        public String reference() {
            return reference;
        }

        public BigDecimal total() {
            return total;
        }

        public void markProcessed() {
            this.status = Status.CONFIRMED;
            this.processedAt = LocalDateTime.now();
        }

        @Override
        public String toString() {
            return reference + " - " + total + " € (" + status + ")";
        }
    }

    private enum Status {
        DRAFT,
        CONFIRMED
    }

    /**
     * SRP: classe dédiée à la validation.
     */
    private interface OrderValidator {
        void validate(Order order);
    }

    private static final class AmountValidator implements OrderValidator {
        @Override
        public void validate(Order order) {
            if (order.total().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Le montant doit être positif");
            }
        }
    }

    private static final class FraudCheckValidator implements OrderValidator {
        @Override
        public void validate(Order order) {
            if (order.total().compareTo(BigDecimal.valueOf(10_000)) > 0) {
                throw new IllegalArgumentException("Suspicion de fraude");
            }
        }
    }

    /**
     * DIP: l'OrderProcessor dépend d'une abstraction de dépôt.
     */
    private interface OrderRepository {
        void save(Order order);
    }

    private static final class InMemoryOrderRepository implements OrderRepository {
        private final List<Order> storage = new ArrayList<>();

        @Override
        public void save(Order order) {
            storage.add(order);
            System.out.println("[Repo] Commande sauvegardée: " + order);
        }
    }

    private interface NotificationGateway {
        void notifyCustomer(Order order);
    }

    private static final class ConsoleNotificationGateway implements NotificationGateway {
        @Override
        public void notifyCustomer(Order order) {
            System.out.println("[Notification] E-mail envoyé pour " + order.reference());
        }
    }

    /**
     * Classe orchestrant le processus: respecte SRP et DIP.
     */
    private static final class OrderProcessor {
        private final List<OrderValidator> validators;
        private final OrderRepository repository;
        private final NotificationGateway notificationGateway;

        OrderProcessor(List<OrderValidator> validators, OrderRepository repository,
                       NotificationGateway notificationGateway) {
            this.validators = validators;
            this.repository = repository;
            this.notificationGateway = notificationGateway;
        }

        void process(Order order) {
            validators.forEach(validator -> validator.validate(order));
            order.markProcessed();
            repository.save(order);
            notificationGateway.notifyCustomer(order);
        }
    }
}
