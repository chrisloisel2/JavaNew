package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

/**
 * Record DTO pour les auteurs.
 * Utilise les Records Java pour une immutabilit\u00e9 et une concision optimales.
 */
public record AuthorDTO(
        Long id,

        @NotBlank(message = "Le pr\u00e9nom est obligatoire")
        String firstName,

        @NotBlank(message = "Le nom est obligatoire")
        String lastName,

        LocalDate birthDate,

        String nationality,

        String biography
) {
    /**
     * Constructeur compact avec validation
     */
    public AuthorDTO {
        // Normalisation des donn\u00e9es
        if (firstName != null) {
            firstName = firstName.trim();
        }
        if (lastName != null) {
            lastName = lastName.trim();
        }
    }

    /**
     * Retourne le nom complet de l'auteur
     */
    public String fullName() {
        return firstName + " " + lastName;
    }
}
