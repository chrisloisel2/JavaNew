package com.exercices.poo.tp2;

import java.util.List;
import java.util.Objects;

public final class CheckoutService {

    private final OrderCreator orderCreator;
    private final OrderValidator orderValidator;
    private final OrderDispatcher dispatcher;
    private final PaymentMethod paymentMethod;

    public CheckoutService(OrderCreator orderCreator, OrderValidator orderValidator,
                           OrderDispatcher dispatcher, PaymentMethod paymentMethod) {
        this.orderCreator = Objects.requireNonNull(orderCreator);
        this.orderValidator = Objects.requireNonNull(orderValidator);
        this.dispatcher = Objects.requireNonNull(dispatcher);
        this.paymentMethod = Objects.requireNonNull(paymentMethod);
    }

    public Order checkout(String customerEmail, List<String> items, double amount) {
        Order order = orderCreator.create(customerEmail, items.toArray(String[]::new));
        orderValidator.validate(order);
        if (paymentMethod.process(amount)) {
            dispatcher.dispatch(order);
        }
        return order;
    }
}
