package com.example.patterns.behavioral.strategy;

@FunctionalInterface
public interface PricingStrategy {
    double apply(double basePrice);

    static PricingStrategy discount(double percent) {
        return price -> price * (1 - percent);
    }

    static PricingStrategy premium(double surcharge) {
        return price -> price + surcharge;
    }
}
