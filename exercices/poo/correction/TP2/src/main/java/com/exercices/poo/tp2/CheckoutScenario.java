package com.exercices.poo.tp2;

import java.time.YearMonth;
import java.util.List;

public final class CheckoutScenario {

    private CheckoutScenario() {
    }

    public static void main(String[] args) {
        OrderCreator creator = new DefaultOrderCreator();
        OrderValidator validator = new BasicOrderValidator();
        OrderDispatcher dispatcher = order -> System.out.println("[MOCK] Expédition programmée pour " + order.id());
        PaymentMethod paymentMethod = new CardPayment("123456789012", YearMonth.now().plusYears(2));

        CheckoutService checkout = new CheckoutService(creator, validator, dispatcher, paymentMethod);
        checkout.checkout("client@example.com", List.of("Livre", "Clavier"), 129.99);

        PaymentMethod wallet = new WalletPayment("wallet-42", 200.0);
        CheckoutService checkoutWallet = new CheckoutService(creator, validator, dispatcher, wallet);
        checkoutWallet.checkout("client@example.com", List.of("Casque"), 59.0);
    }
}
