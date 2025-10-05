package com.exercices.poo.tp2;

import java.time.Instant;
import java.util.List;

public record Order(String id, String customerEmail, List<String> items, Instant createdAt) {
}
