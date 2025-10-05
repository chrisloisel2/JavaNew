package com.exercices.poo.tp2;

sealed interface PaymentMethod permits CardPayment, WalletPayment {
    boolean process(double amount);
}
