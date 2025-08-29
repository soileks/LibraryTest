package com.example.library.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "loans", indexes = {
        @Index(name = "idx_loan_date", columnList = "loanDate"),
        @Index(name = "idx_client_book", columnList = "client_id,book_id")
})
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Client is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @NotNull(message = "Book is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @NotNull(message = "Loan date is required")
    @Column(nullable = false)
    private LocalDate loanDate;

    @Column(nullable = false)
    private boolean returned = false;

    public Loan() {}

    public Loan(Client client, Book book, LocalDate loanDate) {
        this.client = client;
        this.book = book;
        this.loanDate = loanDate;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }
    public boolean isReturned() { return returned; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public LocalDate getLoanDate() { return loanDate; }

    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }
}
