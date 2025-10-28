package com.example.backend.config;

import com.example.backend.model.Author;
import com.example.backend.model.Book;
import com.example.backend.model.User;
import com.example.backend.repository.AuthorRepository;
import com.example.backend.repository.BookRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Cr\u00e9er des auteurs
        Author author1 = new Author();
        author1.setFirstName("Victor");
        author1.setLastName("Hugo");
        author1.setBirthDate(LocalDate.of(1802, 2, 26));
        author1.setNationality("Fran\u00e7aise");
        author1.setBiography("Po\u00e8te, dramaturge, \u00e9crivain, romancier et dessinateur romantique fran\u00e7ais");
        authorRepository.save(author1);

        Author author2 = new Author();
        author2.setFirstName("Albert");
        author2.setLastName("Camus");
        author2.setBirthDate(LocalDate.of(1913, 11, 7));
        author2.setNationality("Fran\u00e7aise");
        author2.setBiography("\u00c9crivain, philosophe, romancier, dramaturge, journaliste et essayiste fran\u00e7ais");
        authorRepository.save(author2);

        Author author3 = new Author();
        author3.setFirstName("Moli\u00e8re");
        author3.setLastName("Pocquelin");
        author3.setBirthDate(LocalDate.of(1622, 1, 15));
        author3.setNationality("Fran\u00e7aise");
        author3.setBiography("Com\u00e9dien et dramaturge fran\u00e7ais");
        authorRepository.save(author3);

        Author author4 = new Author();
        author4.setFirstName("George");
        author4.setLastName("Orwell");
        author4.setBirthDate(LocalDate.of(1903, 6, 25));
        author4.setNationality("Britannique");
        author4.setBiography("\u00c9crivain, essayiste et journaliste britannique");
        authorRepository.save(author4);

        // Cr\u00e9er des livres
        Book book1 = new Book();
        book1.setTitle("Les Mis\u00e9rables");
        book1.setIsbn("978-2070409228");
        book1.setAuthor(author1);
        book1.setPublicationDate(LocalDate.of(1862, 4, 3));
        book1.setGenre("Roman historique");
        book1.setDescription("Un roman social et historique qui d\u00e9crit la vie de mis\u00e9reux dans Paris au XIXe si\u00e8cle");
        book1.setTotalCopies(3);
        book1.setAvailableCopies(3);
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setTitle("Notre-Dame de Paris");
        book2.setIsbn("978-2070413089");
        book2.setAuthor(author1);
        book2.setPublicationDate(LocalDate.of(1831, 3, 16));
        book2.setGenre("Roman historique");
        book2.setDescription("Roman se d\u00e9roulant dans le Paris du XVe si\u00e8cle");
        book2.setTotalCopies(2);
        book2.setAvailableCopies(2);
        bookRepository.save(book2);

        Book book3 = new Book();
        book3.setTitle("L'\u00c9tranger");
        book3.setIsbn("978-2070360024");
        book3.setAuthor(author2);
        book3.setPublicationDate(LocalDate.of(1942, 6, 15));
        book3.setGenre("Roman philosophique");
        book3.setDescription("Premier roman d'Albert Camus, paru en 1942");
        book3.setTotalCopies(4);
        book3.setAvailableCopies(4);
        bookRepository.save(book3);

        Book book4 = new Book();
        book4.setTitle("La Peste");
        book4.setIsbn("978-2070360420");
        book4.setAuthor(author2);
        book4.setPublicationDate(LocalDate.of(1947, 6, 10));
        book4.setGenre("Roman philosophique");
        book4.setDescription("Roman d'Albert Camus publié en 1947");
        book4.setTotalCopies(3);
        book4.setAvailableCopies(3);
        bookRepository.save(book4);

        Book book5 = new Book();
        book5.setTitle("Le Tartuffe");
        book5.setIsbn("978-2070387090");
        book5.setAuthor(author3);
        book5.setPublicationDate(LocalDate.of(1669, 2, 5));
        book5.setGenre("Com\u00e9die");
        book5.setDescription("Com\u00e9die en cinq actes en vers");
        book5.setTotalCopies(2);
        book5.setAvailableCopies(2);
        bookRepository.save(book5);

        Book book6 = new Book();
        book6.setTitle("1984");
        book6.setIsbn("978-2070368228");
        book6.setAuthor(author4);
        book6.setPublicationDate(LocalDate.of(1949, 6, 8));
        book6.setGenre("Science-fiction dystopique");
        book6.setDescription("Roman dystopique d\u00e9crivant un r\u00e9gime totalitaire");
        book6.setTotalCopies(5);
        book6.setAvailableCopies(5);
        bookRepository.save(book6);

        Book book7 = new Book();
        book7.setTitle("La Ferme des animaux");
        book7.setIsbn("978-2070375165");
        book7.setAuthor(author4);
        book7.setPublicationDate(LocalDate.of(1945, 8, 17));
        book7.setGenre("Fable satirique");
        book7.setDescription("Court roman allégorique d\u00e9crivant une ferme dans laquelle les animaux se r\u00e9voltent");
        book7.setTotalCopies(3);
        book7.setAvailableCopies(3);
        bookRepository.save(book7);

        // Cr\u00e9er des utilisateurs
        User user1 = new User();
        user1.setUsername("admin");
        user1.setPassword(passwordEncoder.encode("admin123"));
        user1.setEmail("admin@library.com");
        user1.setFirstName("Admin");
        user1.setLastName("Biblioth\u00e8que");
        user1.setPhoneNumber("0123456789");
        user1.setRole(User.Role.ADMIN);
        user1.setActive(true);
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("john.doe");
        user2.setPassword(passwordEncoder.encode("password"));
        user2.setEmail("john.doe@email.com");
        user2.setFirstName("John");
        user2.setLastName("Doe");
        user2.setPhoneNumber("0612345678");
        user2.setRole(User.Role.USER);
        user2.setActive(true);
        userRepository.save(user2);

        User user3 = new User();
        user3.setUsername("jane.smith");
        user3.setPassword(passwordEncoder.encode("password"));
        user3.setEmail("jane.smith@email.com");
        user3.setFirstName("Jane");
        user3.setLastName("Smith");
        user3.setPhoneNumber("0698765432");
        user3.setRole(User.Role.USER);
        user3.setActive(true);
        userRepository.save(user3);

        User librarian = new User();
        librarian.setUsername("librarian");
        librarian.setPassword(passwordEncoder.encode("librarian123"));
        librarian.setEmail("librarian@library.com");
        librarian.setFirstName("Marie");
        librarian.setLastName("Biblioth\u00e9caire");
        librarian.setPhoneNumber("0145678901");
        librarian.setRole(User.Role.LIBRARIAN);
        librarian.setActive(true);
        userRepository.save(librarian);

        System.out.println("=== Donn\u00e9es de test initialis\u00e9es avec succ\u00e8s ===");
        System.out.println("Auteurs cr\u00e9\u00e9s: " + authorRepository.count());
        System.out.println("Livres cr\u00e9\u00e9s: " + bookRepository.count());
        System.out.println("Utilisateurs cr\u00e9\u00e9s: " + userRepository.count());
        System.out.println("========================================");
    }
}
