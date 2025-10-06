package com.example.patterns.behavioral.command;

import java.util.ArrayDeque;
import java.util.Queue;

public class CommandScheduler {

    private final Queue<Command> queue = new ArrayDeque<>();

    public void schedule(Command command) {
        queue.add(command);
    }

    public void runAll() {
        while (!queue.isEmpty()) {
            queue.poll().execute();
        }
    }
}
