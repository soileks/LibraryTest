package com.example.library.service;

import com.example.library.dto.BookSearchResult;
import com.example.library.exception.DuplicateIsbnException;
import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public BookSearchResult searchBooks(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Book> bookPage;

        if (!query.isEmpty()) {
            bookPage = bookRepository.searchBooks(query, pageable);
        } else {
            bookPage = bookRepository.findAll(pageable);
        }

        // Генерация номеров страниц
        List<Integer> pageNumbers = Collections.emptyList();
        int totalPages = bookPage.getTotalPages();
        if (totalPages > 0) {
            pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
        }

        return new BookSearchResult(bookPage.getContent(), bookPage, pageNumbers, query);
    }

    public void validateBookIsbn(Book book) {
        boolean isbnExists = book.getId() != null ?
                bookRepository.existsByIsbnAndIdNot(book.getIsbn(), book.getId()) :
                bookRepository.existsByIsbn(book.getIsbn());

        if (isbnExists) {
            throw new DuplicateIsbnException("Book with this ISBN already exists");
        }
    }

    public Book saveBookWithValidation(Book book) {
        validateBookIsbn(book);
        return bookRepository.save(book);
    }
}