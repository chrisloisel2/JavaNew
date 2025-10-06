package com.example.patterns.behavioral.state;

sealed interface TaskState permits DraftState, SubmittedState, CompletedState {
    TaskState submit();
    TaskState complete();
    String name();
}
