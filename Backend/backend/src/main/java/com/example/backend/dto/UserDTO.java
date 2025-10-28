package com.example.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * Record DTO pour les utilisateurs
 */
public record UserDTO(
        Long id,

        @NotBlank(message = "Le nom d'utilisateur est obligatoire")
        String username,

        @Email(message = "L'email doit \u00eatre valide")
        @NotBlank(message = "L'email est obligatoire")
        String email,

        @NotBlank(message = "Le pr\u00e9nom est obligatoire")
        String firstName,

        @NotBlank(message = "Le nom est obligatoire")
        String lastName,

        String phoneNumber,

        String role,

        Boolean active,

        LocalDateTime createdAt
) {
    /**
     * Constructeur compact
     */
    public UserDTO {
        if (username != null) {
            username = username.toLowerCase().trim();
        }
        if (email != null) {
            email = email.toLowerCase().trim();
        }
    }

    /**
     * Retourne le nom complet de l'utilisateur
     */
    public String fullName() {
        return firstName + " " + lastName;
    }

    /**
     * V\u00e9rifie si l'utilisateur est actif
     */
    public boolean isActive() {
        return active != null && active;
    }
}
