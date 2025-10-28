package com.example.backend.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Record DTO pour les requ\u00eates d'emprunt
 */
public record LoanRequestDTO(
        @NotNull(message = "L'ID du livre est obligatoire")
        Long bookId,

        @NotNull(message = "L'ID de l'utilisateur est obligatoire")
        Long userId,

        String notes
) {
    /**
     * Constructeur compact avec normalisation
     */
    public LoanRequestDTO {
        if (notes != null) {
            notes = notes.trim();
        }
    }
}
