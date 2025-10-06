package com.example.patterns.behavioral.templatemethod;

public class SalesReport extends ReportGenerator {
    @Override
    protected String body() {
        return "Sales body\n";
    }
}
