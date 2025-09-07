package com.example.library.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "books", indexes = {
        @Index(name = "idx_isbn", columnList = "isbn"),
        @Index(name = "idx_author", columnList = "author"),
        @Index(name = "idx_title", columnList = "title")
})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false, length = 300)
    private String title;

    @NotBlank(message = "Author is required")
    @Column(nullable = false, length = 300)
    @Pattern(regexp = "^[\\p{L} \\-'’.]+$",
            message = "Author name can only contain letters, spaces, hyphens, and apostrophes")
    private String author;

    @NotBlank(message = "ISBN is required")
    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    public Book() {}

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
}
