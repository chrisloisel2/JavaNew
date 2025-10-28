package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

/**
 * Record DTO pour les livres avec validation int\u00e9gr\u00e9e
 */
public record BookDTO(
        Long id,

        @NotBlank(message = "Le titre est obligatoire")
        String title,

        @NotBlank(message = "L'ISBN est obligatoire")
        String isbn,

        @NotNull(message = "L'ID de l'auteur est obligatoire")
        Long authorId,

        String authorName,

        LocalDate publicationDate,

        String genre,

        String description,

        @Positive(message = "Le nombre de copies doit \u00eatre positif")
        Integer totalCopies,

        Integer availableCopies,

        Boolean available
) {
    /**
     * Constructeur compact avec normalisation
     */
    public BookDTO {
        if (title != null) {
            title = title.trim();
        }
        if (isbn != null) {
            isbn = isbn.replaceAll("[^0-9X-]", "");
        }
    }

    /**
     * V\u00e9rifie si le livre est en rupture de stock
     */
    public boolean isOutOfStock() {
        return availableCopies == null || availableCopies == 0;
    }

    /**
     * Calcule le taux de disponibilit\u00e9
     */
    public double availabilityRate() {
        if (totalCopies == null || totalCopies == 0) {
            return 0.0;
        }
        return (double) (availableCopies != null ? availableCopies : 0) / totalCopies * 100;
    }
}
