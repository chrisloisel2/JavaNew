package com.example.patterns.behavioral.state;

final class CompletedState implements TaskState {
    @Override
    public TaskState submit() {
        return this;
    }

    @Override
    public TaskState complete() {
        return this;
    }

    @Override
    public String name() {
        return "COMPLETED";
    }
}
