package com.example.backend.repository;

import com.example.backend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorId(Long authorId);

    List<Book> findByGenre(String genre);

    List<Book> findByAvailableCopiesGreaterThan(Integer availableCopies);

    @Query("SELECT b FROM Book b WHERE b.availableCopies > 0")
    List<Book> findAllAvailableBooks();

    @Query("SELECT b FROM Book b WHERE b.author.lastName LIKE %?1%")
    List<Book> findByAuthorLastName(String authorLastName);
}
