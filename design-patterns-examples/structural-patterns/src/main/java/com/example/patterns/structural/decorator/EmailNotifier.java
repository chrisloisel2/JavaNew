package com.example.patterns.structural.decorator;

public class EmailNotifier implements Notifier {
    @Override
    public String send(String message) {
        return "Email: " + message;
    }
}
