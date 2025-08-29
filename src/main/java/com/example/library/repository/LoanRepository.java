package com.example.library.repository;

import com.example.library.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("SELECT l FROM Loan l JOIN FETCH l.client JOIN FETCH l.book")
    List<Loan> findAllWithClientAndBook();

    @Query("SELECT l FROM Loan l JOIN FETCH l.client JOIN FETCH l.book WHERE l.returned = false")
    List<Loan> findByReturnedFalseWithClientAndBook();

    boolean existsByClientIdAndBookIdAndReturnedFalse(Long clientId, Long bookId);
}