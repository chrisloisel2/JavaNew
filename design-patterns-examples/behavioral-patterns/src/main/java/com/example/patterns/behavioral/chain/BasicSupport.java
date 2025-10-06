package com.example.patterns.behavioral.chain;

public class BasicSupport extends SupportHandler {
    @Override
    protected boolean canHandle(String issue) {
        return issue.contains("password");
    }

    @Override
    protected String solve(String issue) {
        return "Reset du mot de passe effectué";
    }
}
