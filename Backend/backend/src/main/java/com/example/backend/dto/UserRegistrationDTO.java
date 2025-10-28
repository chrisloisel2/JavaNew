package com.example.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Record DTO pour l'enregistrement d'utilisateurs
 */
public record UserRegistrationDTO(
        @NotBlank(message = "Le nom d'utilisateur est obligatoire")
        @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit contenir entre 3 et 50 caract\u00e8res")
        String username,

        @NotBlank(message = "Le mot de passe est obligatoire")
        @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caract\u00e8res")
        String password,

        @Email(message = "L'email doit \u00eatre valide")
        @NotBlank(message = "L'email est obligatoire")
        String email,

        @NotBlank(message = "Le pr\u00e9nom est obligatoire")
        String firstName,

        @NotBlank(message = "Le nom est obligatoire")
        String lastName,

        String phoneNumber
) {
    /**
     * Constructeur compact avec normalisation
     */
    public UserRegistrationDTO {
        if (username != null) {
            username = username.toLowerCase().trim();
        }
        if (email != null) {
            email = email.toLowerCase().trim();
        }
        if (firstName != null) {
            firstName = firstName.trim();
        }
        if (lastName != null) {
            lastName = lastName.trim();
        }
    }
}
