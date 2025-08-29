package com.example.library.dto;

import com.example.library.model.Book;
import org.springframework.data.domain.Page;

import java.util.List;

public class BookSearchResult {
    private List<Book> books;
    private Page<Book> bookPage;
    private List<Integer> pageNumbers;
    private String searchQuery;

    public BookSearchResult(List<Book> books, Page<Book> bookPage,
                            List<Integer> pageNumbers, String searchQuery) {
        this.books = books;
        this.bookPage = bookPage;
        this.pageNumbers = pageNumbers;
        this.searchQuery = searchQuery;
    }

    public List<Book> getBooks() {
        return books;
    }

    public Page<Book> getBookPage() {
        return bookPage;
    }

    public List<Integer> getPageNumbers() {
        return pageNumbers;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void setBookPage(Page<Book> bookPage) {
        this.bookPage = bookPage;
    }

    public void setPageNumbers(List<Integer> pageNumbers) {
        this.pageNumbers = pageNumbers;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
}
