package com.example.java10;

import java.util.List;
import java.util.Map;

public class VarShowcase {

    public String formatMessage() {
        var message = "Hello Java"; // inféré en String
        var version = 10;
        return message + " " + version;
    }

    public int countWords(List<String> words) {
        var totalLength = 0;
        for (var word : words) {
            totalLength += word.length();
        }
        return totalLength;
    }

    public String describeMap(Map<String, Integer> scores) {
        var builder = new StringBuilder();
        scores.forEach((var key, var value) -> builder.append(key).append(": ").append(value).append('\n'));
        return builder.toString();
    }
}
