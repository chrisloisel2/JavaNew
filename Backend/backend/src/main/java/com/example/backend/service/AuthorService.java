package com.example.backend.service;

import com.example.backend.dto.AuthorDTO;
import com.example.backend.model.Author;
import com.example.backend.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorService {

    private final AuthorRepository authorRepository;

    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auteur introuvable avec l'ID: " + id));
        return convertToDTO(author);
    }

    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = convertToEntity(authorDTO);
        Author savedAuthor = authorRepository.save(author);
        return convertToDTO(savedAuthor);
    }

    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auteur introuvable avec l'ID: " + id));

        author.setFirstName(authorDTO.firstName());
        author.setLastName(authorDTO.lastName());
        author.setBirthDate(authorDTO.birthDate());
        author.setNationality(authorDTO.nationality());
        author.setBiography(authorDTO.biography());

        Author updatedAuthor = authorRepository.save(author);
        return convertToDTO(updatedAuthor);
    }

    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new RuntimeException("Auteur introuvable avec l'ID: " + id);
        }
        authorRepository.deleteById(id);
    }

    public List<AuthorDTO> searchAuthorsByLastName(String lastName) {
        return authorRepository.findByLastNameContainingIgnoreCase(lastName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AuthorDTO convertToDTO(Author author) {
        return new AuthorDTO(
                author.getId(),
                author.getFirstName(),
                author.getLastName(),
                author.getBirthDate(),
                author.getNationality(),
                author.getBiography()
        );
    }

    private Author convertToEntity(AuthorDTO dto) {
        Author author = new Author();
        author.setFirstName(dto.firstName());
        author.setLastName(dto.lastName());
        author.setBirthDate(dto.birthDate());
        author.setNationality(dto.nationality());
        author.setBiography(dto.biography());
        return author;
    }
}
