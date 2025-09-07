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

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public ClientSearchResult searchClients(String query, String searchType, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Client> clientPage;

        if (query != null && !query.trim().isEmpty()) {
            switch (searchType != null ? searchType : "all") {
                case "name":
                    clientPage = clientRepository.findByFullName(query, pageable);
                    break;
                case "birthDate":
                    try {
                        LocalDate birthDate = LocalDate.parse(query);
                        clientPage = clientRepository.findByBirthDate(birthDate, pageable);
                    } catch (DateTimeParseException e) {
                        clientPage = Page.empty(pageable);
                    }
                    break;
                case "all":
                default:
                    clientPage = clientRepository.searchClients(query, pageable);
                    break;
            }
        } else {
            clientPage = clientRepository.findAll(pageable);
        }

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