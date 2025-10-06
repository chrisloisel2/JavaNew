package com.example.patterns.behavioral.state;

final class SubmittedState implements TaskState {
    @Override
    public TaskState submit() {
        return this;
    }

    @Override
    public TaskState complete() {
        return new CompletedState();
    }

    @Override
    public String name() {
        return "SUBMITTED";
    }
}
