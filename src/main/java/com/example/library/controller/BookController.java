package com.example.library.controller;

import com.example.library.dto.BookSearchResult;
import com.example.library.exception.DuplicateIsbnException;
import com.example.library.model.Book;
import com.example.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String listBooks(
            @RequestParam(value = "page", defaultValue = "1") @Min(1) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(value = "search", defaultValue = "") String searchQuery,
            @RequestParam(value = "searchType", defaultValue = "all") String searchType,
            Model model) {

        BookSearchResult result = bookService.searchBooks(searchQuery, searchType, page, size);

        model.addAttribute("books", result.getBooks());
        model.addAttribute("bookPage", result.getBookPage());
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("searchType", searchType);
        model.addAttribute("pageNumbers", result.getPageNumbers());

        return "books";
    }

    @GetMapping("/new")
    public String showBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "book-form";
    }

    @PostMapping("/save")
    public String saveBook(@Valid @ModelAttribute Book book, BindingResult result) {

        if (result.hasErrors()) {
            return "book-form";
        }

        try {
            bookService.saveBookWithValidation(book);
        } catch (DuplicateIsbnException e) {
            result.rejectValue("isbn", "error.book", e.getMessage());
            return "book-form";
        }

        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book ID: " + id));
        model.addAttribute("book", book);
        return "book-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

}