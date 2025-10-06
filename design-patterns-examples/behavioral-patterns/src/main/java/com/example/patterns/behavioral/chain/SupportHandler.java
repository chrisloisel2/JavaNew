package com.example.patterns.behavioral.chain;

public abstract class SupportHandler {

    private SupportHandler next;

    public SupportHandler linkWith(SupportHandler next) {
        this.next = next;
        return next;
    }

    public String handle(String issue) {
        if (canHandle(issue)) {
            return solve(issue);
        }
        if (next != null) {
            return next.handle(issue);
        }
        return "Aucun support disponible";
    }

    protected abstract boolean canHandle(String issue);

    protected abstract String solve(String issue);
}
