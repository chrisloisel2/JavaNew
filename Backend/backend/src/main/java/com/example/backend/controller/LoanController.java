package com.example.backend.controller;

import com.example.backend.dto.LoanDTO;
import com.example.backend.dto.LoanRequestDTO;
import com.example.backend.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @GetMapping
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanDTO> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getLoanById(id));
    }

    @PostMapping
    public ResponseEntity<LoanDTO> createLoan(@Valid @RequestBody LoanRequestDTO loanRequestDTO) {
        LoanDTO created = loanService.createLoan(loanRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<LoanDTO> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.returnBook(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanDTO>> getLoansByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(loanService.getLoansByUser(userId));
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<LoanDTO>> getActiveLoansByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(loanService.getActiveLoansByUser(userId));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<LoanDTO>> getOverdueLoans() {
        return ResponseEntity.ok(loanService.getOverdueLoans());
    }
}
