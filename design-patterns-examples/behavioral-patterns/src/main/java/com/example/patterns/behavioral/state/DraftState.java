package com.example.patterns.behavioral.state;

final class DraftState implements TaskState {
    @Override
    public TaskState submit() {
        return new SubmittedState();
    }

    @Override
    public TaskState complete() {
        return this;
    }

    @Override
    public String name() {
        return "DRAFT";
    }
}
