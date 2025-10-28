package com.example.backend.service;

import com.example.backend.dto.LoanDTO;
import com.example.backend.dto.LoanRequestDTO;
import com.example.backend.model.Book;
import com.example.backend.model.Loan;
import com.example.backend.model.User;
import com.example.backend.repository.BookRepository;
import com.example.backend.repository.LoanRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    private static final int MAX_LOANS_PER_USER = 5;

    public List<LoanDTO> getAllLoans() {
        return loanRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LoanDTO getLoanById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emprunt introuvable avec l'ID: " + id));
        return convertToDTO(loan);
    }

    public LoanDTO createLoan(LoanRequestDTO loanRequestDTO) {
        User user = userRepository.findById(loanRequestDTO.userId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Book book = bookRepository.findById(loanRequestDTO.bookId())
                .orElseThrow(() -> new RuntimeException("Livre introuvable"));

        // V\u00e9rifier si l'utilisateur a atteint la limite d'emprunts
        Long activeLoans = loanRepository.countActiveLoansForUser(user.getId());
        if (activeLoans >= MAX_LOANS_PER_USER) {
            throw new RuntimeException("Limite d'emprunts atteinte pour cet utilisateur");
        }

        // V\u00e9rifier la disponibilit\u00e9 du livre
        if (!book.isAvailable()) {
            throw new RuntimeException("Ce livre n'est pas disponible");
        }

        // Cr\u00e9er l'emprunt
        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusWeeks(2));
        loan.setStatus(Loan.LoanStatus.ACTIVE);
        loan.setNotes(loanRequestDTO.notes());

        // R\u00e9duire le nombre de copies disponibles
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        Loan savedLoan = loanRepository.save(loan);
        return convertToDTO(savedLoan);
    }

    public LoanDTO returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Emprunt introuvable"));

        if (loan.getStatus() != Loan.LoanStatus.ACTIVE) {
            throw new RuntimeException("Cet emprunt n'est pas actif");
        }

        loan.setReturnDate(LocalDate.now());
        loan.setStatus(Loan.LoanStatus.RETURNED);

        // Augmenter le nombre de copies disponibles
        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        Loan updatedLoan = loanRepository.save(loan);
        return convertToDTO(updatedLoan);
    }

    public List<LoanDTO> getLoansByUser(Long userId) {
        return loanRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<LoanDTO> getActiveLoansByUser(Long userId) {
        return loanRepository.findActiveLoansForUser(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<LoanDTO> getOverdueLoans() {
        return loanRepository.findOverdueLoans(LocalDate.now()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private LoanDTO convertToDTO(Loan loan) {
        return new LoanDTO(
                loan.getId(),
                loan.getBook().getId(),
                loan.getBook().getTitle(),
                loan.getUser().getId(),
                loan.getUser().getFirstName() + " " + loan.getUser().getLastName(),
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.getReturnDate(),
                loan.getStatus().name(),
                loan.getNotes(),
                loan.isOverdue()
        );
    }
}
