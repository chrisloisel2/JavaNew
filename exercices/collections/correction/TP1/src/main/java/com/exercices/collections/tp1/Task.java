package com.exercices.collections.tp1;

import java.util.Set;

public record Task(String id, String titre, Status status, Set<String> labels) {
}
