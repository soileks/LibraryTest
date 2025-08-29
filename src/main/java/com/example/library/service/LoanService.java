package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.model.Client;
import com.example.library.model.Loan;
import com.example.library.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoanService {

    private final LoanRepository loanRepository;
    private final ClientService clientService;
    private final BookService bookService;

    @Autowired
    public LoanService(LoanRepository loanRepository, ClientService clientService, BookService bookService) {
        this.loanRepository = loanRepository;
        this.clientService = clientService;
        this.bookService = bookService;
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAllWithClientAndBook();
    }

    public Loan saveLoan(Loan loan) {
        return loanRepository.save(loan);
    }

    public void deleteLoan(Long id) {
        loanRepository.deleteById(id);
    }

    public void returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        loan.setReturned(true);
        loanRepository.save(loan);
    }

    public List<Loan> getActiveLoans() {
        return loanRepository.findByReturnedFalseWithClientAndBook();
    }

    public List<Client> getAvailableClients() {
        return clientService.getAllClients();
    }

    public List<Book> getAvailableBooks() {
        return bookService.getAllBooks();
    }

    public Loan createLoan(Long clientId, Long bookId, LocalDate loanDate) {
        Client client = clientService.getClientById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid client ID"));
        Book book = bookService.getBookById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book ID"));

        // Проверка: не взял ли клиент уже эту книгу
        if (loanRepository.existsByClientIdAndBookIdAndReturnedFalse(clientId, bookId)) {
            throw new IllegalStateException("Client already has this book");
        }

        Loan loan = new Loan(client, book, loanDate);
        return loanRepository.save(loan);
    }

    public List<Map<String, Object>> getActiveReadersData() {
        List<Loan> activeLoans = getActiveLoans();

        return activeLoans.stream()
                .map(this::convertLoanToMap)
                .collect(Collectors.toList());
    }

    private Map<String, Object> convertLoanToMap(Loan loan) {
        Map<String, Object> readerInfo = new HashMap<>();
        readerInfo.put("fullName", loan.getClient().getFullName());
        readerInfo.put("birthDate", loan.getClient().getBirthDate());
        readerInfo.put("bookTitle", loan.getBook().getTitle());
        readerInfo.put("bookAuthor", loan.getBook().getAuthor());
        readerInfo.put("bookIsbn", loan.getBook().getIsbn());
        readerInfo.put("loanDate", loan.getLoanDate());
        return readerInfo;
    }

}