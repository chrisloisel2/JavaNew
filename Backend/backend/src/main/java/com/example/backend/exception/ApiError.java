package com.example.backend.exception;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Record pour repr\u00e9senter une erreur d'API de mani\u00e8re immutable
 */
public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<String> details
) {
    /**
     * Constructeur compact avec validation
     */
    public ApiError {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
        if (details == null) {
            details = List.of();
        }
    }

    /**
     * Constructeur simplifi\u00e9 sans d\u00e9tails
     */
    public ApiError(LocalDateTime timestamp, int status, String error, String message, String path) {
        this(timestamp, status, error, message, path, List.of());
    }

    /**
     * V\u00e9rifie si l'erreur a des d\u00e9tails
     */
    public boolean hasDetails() {
        return details != null && !details.isEmpty();
    }
}
