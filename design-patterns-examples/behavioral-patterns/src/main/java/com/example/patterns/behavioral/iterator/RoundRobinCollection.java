package com.example.patterns.behavioral.iterator;

import java.util.Iterator;
import java.util.List;

public class RoundRobinCollection implements Iterable<String> {

    private final List<String> items;

    public RoundRobinCollection(List<String> items) {
        this.items = List.copyOf(items);
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<>() {
            private int index;

            @Override
            public boolean hasNext() {
                return !items.isEmpty();
            }

            @Override
            public String next() {
                String value = items.get(index);
                index = (index + 1) % items.size();
                return value;
            }
        };
    }
}
