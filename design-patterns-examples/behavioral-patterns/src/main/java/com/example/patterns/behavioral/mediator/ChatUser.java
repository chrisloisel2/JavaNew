package com.example.patterns.behavioral.mediator;

public abstract class ChatUser {

    private ChatMediator mediator;
    private final String name;

    protected ChatUser(String name) {
        this.name = name;
    }

    void setMediator(ChatMediator mediator) {
        this.mediator = mediator;
    }

    public void send(String message) {
        mediator.broadcast(this, name + ": " + message);
    }

    public abstract void receive(String message);
}
