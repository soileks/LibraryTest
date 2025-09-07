package com.example.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class ActiveReaderDto {
    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("birthDate")
    private LocalDate birthDate;
    @JsonProperty("bookTitle")
    private String bookTitle;
    @JsonProperty("bookAuthor")
    private String bookAuthor;
    @JsonProperty("bookIsbn")
    private String bookIsbn;
    @JsonProperty("loanDate")
    private LocalDate loanDate;

    public ActiveReaderDto() {}

    public ActiveReaderDto(String fullName, LocalDate birthDate, String bookTitle,
                           String bookAuthor, String bookIsbn, LocalDate loanDate) {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookIsbn = bookIsbn;
        this.loanDate = loanDate;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }
}
