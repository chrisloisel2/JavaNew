package com.example.backend.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Record DTO pour les emprunts avec logique m\u00e9tier int\u00e9gr\u00e9e
 */
public record LoanDTO(
        Long id,

        @NotNull(message = "L'ID du livre est obligatoire")
        Long bookId,

        String bookTitle,

        @NotNull(message = "L'ID de l'utilisateur est obligatoire")
        Long userId,

        String userName,

        LocalDate loanDate,

        LocalDate dueDate,

        LocalDate returnDate,

        String status,

        String notes,

        Boolean overdue
) {
    /**
     * Calcule le nombre de jours restants avant la date limite
     */
    public long daysUntilDue() {
        if (dueDate == null || returnDate != null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
    }

    /**
     * Calcule le nombre de jours de retard
     */
    public long daysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    /**
     * V\u00e9rifie si l'emprunt est en retard
     */
    public boolean isOverdue() {
        return overdue != null && overdue;
    }

    /**
     * V\u00e9rifie si l'emprunt est actif
     */
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }

    /**
     * Calcule la dur\u00e9e totale de l'emprunt
     */
    public long totalLoanDays() {
        if (loanDate == null) {
            return 0;
        }
        LocalDate endDate = returnDate != null ? returnDate : LocalDate.now();
        return ChronoUnit.DAYS.between(loanDate, endDate);
    }
}
