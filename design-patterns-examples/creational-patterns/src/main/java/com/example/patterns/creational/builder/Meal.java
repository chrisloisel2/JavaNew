package com.example.patterns.creational.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder pour composer un menu étape par étape.
 */
public class Meal {

    private final List<String> courses;

    private Meal(Builder builder) {
        this.courses = List.copyOf(builder.courses);
    }

    public List<String> courses() {
        return courses;
    }

    public static class Builder {
        private final List<String> courses = new ArrayList<>();

        public Builder addCourse(String course) {
            courses.add(course);
            return this;
        }

        public Meal build() {
            return new Meal(this);
        }
    }
}
