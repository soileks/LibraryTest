package com.example.library.dto;

import com.example.library.model.Client;
import org.springframework.data.domain.Page;

import java.util.List;

public class ClientSearchResult {
    private List<Client> clients;
    private Page<Client> clientPage;
    private List<Integer> pageNumbers;
    private String searchQuery;

    public ClientSearchResult(List<Client> clients, Page<Client> clientPage,
                              List<Integer> pageNumbers, String searchQuery) {
        this.clients = clients;
        this.clientPage = clientPage;
        this.pageNumbers = pageNumbers;
        this.searchQuery = searchQuery;
    }

    public List<Client> getClients() { return clients; }
    public Page<Client> getClientPage() { return clientPage; }
    public List<Integer> getPageNumbers() { return pageNumbers; }
    public String getSearchQuery() { return searchQuery; }
}
