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

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public BookSearchResult searchBooks(String query, String searchType, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Book> bookPage;

        if (!query.trim().isEmpty()) {
            switch (searchType) {
                case "title":
                    bookPage = bookRepository.findByTitle(query, pageable);
                    break;
                case "author":
                    bookPage = bookRepository.findByAuthor(query, pageable);
                    break;
                case "isbn":
                    bookPage = bookRepository.findByIsbn(query, pageable);
                    break;
                case "all":
                default:
                    bookPage = bookRepository.searchBooks(query, pageable);
                    break;
            }
        } else {
            bookPage = bookRepository.findAll(pageable);
        }

        List<Integer> pageNumbers = Collections.emptyList();
        int totalPages = bookPage.getTotalPages();
        if (totalPages > 0) {
            pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
        }

        return new BookSearchResult(bookPage, pageNumbers, query);
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