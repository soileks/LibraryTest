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

import java.util.Optional;


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
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("search") Optional<String> search,
            @RequestParam("searchType") Optional<String> searchType, // Новый параметр
            Model model) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        String searchQuery = search.orElse("");
        String searchTypeValue = searchType.orElse("all"); // По умолчанию "all"

        BookSearchResult result = bookService.searchBooks(searchQuery, searchTypeValue, currentPage, pageSize);

        model.addAttribute("books", result.getBooks());
        model.addAttribute("bookPage", result.getBookPage());
        model.addAttribute("searchQuery", result.getSearchQuery());
        model.addAttribute("searchType", searchTypeValue); // Передаем тип поиска в шаблон
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