package com.example.library.service;

import com.example.library.dto.*;
import com.example.library.model.Book;
import com.example.library.model.Client;
import com.example.library.model.Loan;
import com.example.library.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import com.example.library.mapper.LoanMapper;

import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public void deleteLoan(Long id) {
        loanRepository.deleteById(id);
    }

    public void returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        loan.setReturned(true);
        loanRepository.save(loan);
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

    public List<ActiveReaderDto> getActiveReadersData() {
        List<Loan> activeLoans = loanRepository.findByReturnedFalseWithClientAndBook();

        return activeLoans.stream()
                .map(LoanMapper::convertLoanToDto)
                .collect(Collectors.toList());
    }

    public LoanSearchResult searchLoans(String query, String searchType, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Loan> loanPage;

        if (!query.trim().isEmpty()) {
            switch (searchType) {
                case "client":
                    loanPage = loanRepository.findByClientFullName(query, pageable);
                    break;
                case "bookTitle":
                    loanPage = loanRepository.findByBookTitle(query, pageable);
                    break;
                case "bookAuthor":
                    loanPage = loanRepository.findByBookAuthor(query, pageable);
                    break;
                case "isbn":
                    loanPage = loanRepository.findByBookIsbn(query, pageable);
                    break;
                case "loanDate":
                    try {
                        LocalDate loanDate = LocalDate.parse(query);
                        loanPage = loanRepository.findByLoanDate(loanDate, pageable);
                    } catch (DateTimeParseException e) {
                        loanPage = Page.empty(pageable);
                    }
                    break;
                case "all":
                default:
                    loanPage = loanRepository.searchLoans(query, pageable);
                    break;
            }
        } else {
            loanPage = loanRepository.findAllWithPagination(pageable);
        }

        List<Integer> pageNumbers = Collections.emptyList();
        int totalPages = loanPage.getTotalPages();
        if (totalPages > 0) {
            pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
        }

        return new LoanSearchResult(loanPage, pageNumbers, query);
    }

    public LoanFormData getLoanFormData(String clientQuery, String clientSearchType,
                                        String bookQuery, String bookSearchType,
                                        Long selectedClientId, Long selectedBookId) {

        Client selectedClient = null;
        Book selectedBook = null;
        List<Client> clients = Collections.emptyList();
        List<Book> books = Collections.emptyList();

        if (selectedClientId != null) {
            selectedClient = clientService.getClientById(selectedClientId).orElse(null);
        }

        if (selectedBookId != null) {
            selectedBook = bookService.getBookById(selectedBookId).orElse(null);
        }

        if (!clientQuery.trim().isEmpty() && selectedClientId == null) {
            ClientSearchResult clientResult = clientService.searchClients(clientQuery.trim(), clientSearchType, 1, 10);
            clients = clientResult.getClients();
        }

        if (!bookQuery.trim().isEmpty() && selectedBookId == null) {
            BookSearchResult bookResult = bookService.searchBooks(bookQuery.trim(), bookSearchType, 1, 10);
            books = bookResult.getBooks();
        }

        return new LoanFormData(selectedClient, selectedBook, clients, books);
    }
}