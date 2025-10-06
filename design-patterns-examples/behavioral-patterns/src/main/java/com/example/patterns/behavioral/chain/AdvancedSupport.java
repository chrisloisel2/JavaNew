package com.example.patterns.behavioral.chain;

public class AdvancedSupport extends SupportHandler {
    @Override
    protected boolean canHandle(String issue) {
        return issue.contains("database");
    }

    @Override
    protected String solve(String issue) {
        return "Intervention DBA programmée";
    }
}
