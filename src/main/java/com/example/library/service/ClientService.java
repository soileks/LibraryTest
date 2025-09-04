package com.example.library.service;

import com.example.library.dto.ClientSearchResult;
import com.example.library.model.Client;
import com.example.library.repository.ClientRepository;
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
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public ClientSearchResult searchClients(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Client> clientPage;

        if (!query.isEmpty()) {
            clientPage = clientRepository.searchClients(query, pageable);
        } else {
            clientPage = clientRepository.findAll(pageable);
        }

        // Генерация номеров страниц
        List<Integer> pageNumbers = Collections.emptyList();
        int totalPages = clientPage.getTotalPages();
        if (totalPages > 0) {
            pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
        }

        return new ClientSearchResult(clientPage.getContent(), clientPage, pageNumbers, query);
    }

}