package com.example.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Column(nullable = false)
    private String title;

    @Column(unique = true, nullable = false, length = 13)
    private String isbn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @NotNull(message = "L'auteur est obligatoire")
    private Author author;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(length = 100)
    private String genre;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private Integer totalCopies = 1;

    @Column(nullable = false)
    private Integer availableCopies = 1;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Loan> loans = new ArrayList<>();

    public boolean isAvailable() {
        return availableCopies > 0;
    }
}
