package com.example.backend.service;

import com.example.backend.dto.BookDTO;
import com.example.backend.model.Author;
import com.example.backend.model.Book;
import com.example.backend.repository.AuthorRepository;
import com.example.backend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livre introuvable avec l'ID: " + id));
        return convertToDTO(book);
    }

    public BookDTO createBook(BookDTO bookDTO) {
        Author author = authorRepository.findById(bookDTO.authorId())
                .orElseThrow(() -> new RuntimeException("Auteur introuvable avec l'ID: " + bookDTO.authorId()));

        Book book = convertToEntity(bookDTO);
        book.setAuthor(author);
        Book savedBook = bookRepository.save(book);
        return convertToDTO(savedBook);
    }

    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livre introuvable avec l'ID: " + id));

        Author author = authorRepository.findById(bookDTO.authorId())
                .orElseThrow(() -> new RuntimeException("Auteur introuvable avec l'ID: " + bookDTO.authorId()));

        book.setTitle(bookDTO.title());
        book.setIsbn(bookDTO.isbn());
        book.setAuthor(author);
        book.setPublicationDate(bookDTO.publicationDate());
        book.setGenre(bookDTO.genre());
        book.setDescription(bookDTO.description());
        book.setTotalCopies(bookDTO.totalCopies());
        book.setAvailableCopies(bookDTO.availableCopies());

        Book updatedBook = bookRepository.save(book);
        return convertToDTO(updatedBook);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Livre introuvable avec l'ID: " + id);
        }
        bookRepository.deleteById(id);
    }

    public List<BookDTO> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> getAvailableBooks() {
        return bookRepository.findAllAvailableBooks().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> getBooksByAuthor(Long authorId) {
        return bookRepository.findByAuthorId(authorId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private BookDTO convertToDTO(Book book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getAuthor().getId(),
                book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName(),
                book.getPublicationDate(),
                book.getGenre(),
                book.getDescription(),
                book.getTotalCopies(),
                book.getAvailableCopies(),
                book.isAvailable()
        );
    }

    private Book convertToEntity(BookDTO dto) {
        Book book = new Book();
        book.setTitle(dto.title());
        book.setIsbn(dto.isbn());
        book.setPublicationDate(dto.publicationDate());
        book.setGenre(dto.genre());
        book.setDescription(dto.description());
        book.setTotalCopies(dto.totalCopies());
        book.setAvailableCopies(dto.availableCopies());
        return book;
    }
}
