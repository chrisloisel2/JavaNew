package com.example.backend.repository;

import com.example.backend.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByUserId(Long userId);

    List<Loan> findByBookId(Long bookId);

    List<Loan> findByStatus(Loan.LoanStatus status);

    @Query("SELECT l FROM Loan l WHERE l.user.id = ?1 AND l.status = 'ACTIVE'")
    List<Loan> findActiveLoansForUser(Long userId);

    @Query("SELECT l FROM Loan l WHERE l.book.id = ?1 AND l.status = 'ACTIVE'")
    List<Loan> findActiveLoansByBook(Long bookId);

    @Query("SELECT l FROM Loan l WHERE l.status = 'ACTIVE' AND l.dueDate < ?1")
    List<Loan> findOverdueLoans(LocalDate currentDate);

    @Query("SELECT COUNT(l) FROM Loan l WHERE l.user.id = ?1 AND l.status = 'ACTIVE'")
    Long countActiveLoansForUser(Long userId);
}
