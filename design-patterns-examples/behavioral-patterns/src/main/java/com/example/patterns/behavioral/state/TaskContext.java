package com.example.patterns.behavioral.state;

public class TaskContext {

    private TaskState state = new DraftState();

    public void submit() {
        state = state.submit();
    }

    public void complete() {
        state = state.complete();
    }

    public String status() {
        return state.name();
    }
}
