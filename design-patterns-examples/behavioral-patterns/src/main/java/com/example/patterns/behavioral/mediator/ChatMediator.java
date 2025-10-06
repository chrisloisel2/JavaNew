package com.example.patterns.behavioral.mediator;

import java.util.HashSet;
import java.util.Set;

public class ChatMediator {

    private final Set<ChatUser> users = new HashSet<>();

    public void register(ChatUser user) {
        users.add(user);
        user.setMediator(this);
    }

    void broadcast(ChatUser sender, String message) {
        users.stream()
                .filter(user -> user != sender)
                .forEach(user -> user.receive(message));
    }
}
