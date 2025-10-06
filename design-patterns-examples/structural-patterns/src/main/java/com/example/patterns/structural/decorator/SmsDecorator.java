package com.example.patterns.structural.decorator;

public class SmsDecorator implements Notifier {

    private final Notifier delegate;

    public SmsDecorator(Notifier delegate) {
        this.delegate = delegate;
    }

    @Override
    public String send(String message) {
        return delegate.send(message) + " | SMS: " + message;
    }
}
