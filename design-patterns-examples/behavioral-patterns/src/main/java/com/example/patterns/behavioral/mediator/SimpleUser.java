package com.example.patterns.behavioral.mediator;

public class SimpleUser extends ChatUser {

    public SimpleUser(String name) {
        super(name);
    }

    @Override
    public void receive(String message) {
        System.out.println(message);
    }
}
