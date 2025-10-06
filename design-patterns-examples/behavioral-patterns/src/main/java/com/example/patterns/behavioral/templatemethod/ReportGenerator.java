package com.example.patterns.behavioral.templatemethod;

public abstract class ReportGenerator {

    public final String generate(String title) {
        StringBuilder builder = new StringBuilder();
        builder.append(header(title));
        builder.append(body());
        builder.append(footer());
        return builder.toString();
    }

    protected abstract String body();

    protected String header(String title) {
        return "== " + title + " ==\n";
    }

    protected String footer() {
        return "-- end --\n";
    }
}
