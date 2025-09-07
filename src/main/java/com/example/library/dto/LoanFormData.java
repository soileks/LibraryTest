package com.example.library.dto;

import com.example.library.model.Book;
import com.example.library.model.Client;

import java.util.List;

public class LoanFormData {
    private Client selectedClient;
    private Book selectedBook;
    private List<Client> clients;
    private List<Book> books;

    public LoanFormData(Client selectedClient, Book selectedBook,
                        List<Client> clients, List<Book> books) {
        this.selectedClient = selectedClient;
        this.selectedBook = selectedBook;
        this.clients = clients;
        this.books = books;
    }

    public void setSelectedClient(Client selectedClient) {
        this.selectedClient = selectedClient;
    }

    public void setSelectedBook(Book selectedBook) {
        this.selectedBook = selectedBook;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public Client getSelectedClient() {
        return selectedClient;
    }

    public Book getSelectedBook() {
        return selectedBook;
    }

    public List<Client> getClients() {
        return clients;
    }

    public List<Book> getBooks() {
        return books;
    }
}
