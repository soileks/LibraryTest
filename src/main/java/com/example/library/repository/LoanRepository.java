package com.example.library.repository;

import com.example.library.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query(value = "SELECT l FROM Loan l JOIN FETCH l.client JOIN FETCH l.book",
            countQuery = "SELECT COUNT(l) FROM Loan l")
    Page<Loan> findAllWithPagination(Pageable pageable);

    @Query("SELECT l FROM Loan l JOIN FETCH l.client JOIN FETCH l.book WHERE l.returned = false")
    List<Loan> findByReturnedFalseWithClientAndBook();

    boolean existsByClientIdAndBookIdAndReturnedFalse(Long clientId, Long bookId);

    @Query("SELECT l FROM Loan l WHERE " +
            "LOWER(l.client.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(l.book.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(l.book.author) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "l.book.isbn LIKE CONCAT('%', :query, '%') OR " +
            "CAST(l.loanDate AS string) LIKE CONCAT('%', :query, '%')")
    Page<Loan> searchLoans(@Param("query") String query, Pageable pageable);

    Page<Loan> findByClientFullName(String clientName, Pageable pageable);
    Page<Loan> findByBookTitle(String bookTitle, Pageable pageable);
    Page<Loan> findByBookAuthor(String bookAuthor, Pageable pageable);
    Page<Loan> findByBookIsbn(String isbn, Pageable pageable);
    Page<Loan> findByLoanDate(LocalDate loanDate, Pageable pageable);

}