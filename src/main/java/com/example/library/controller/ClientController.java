package com.example.library.controller;

import com.example.library.dto.ClientSearchResult;
import com.example.library.model.Client;
import com.example.library.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Optional;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public String listClients(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("search") Optional<String> search,
            @RequestParam("searchType") Optional<String> searchType,
            Model model) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        String searchQuery = search.orElse("");
        String searchTypeValue = searchType.orElse("all");

        ClientSearchResult result = clientService.searchClients(searchQuery, searchTypeValue, currentPage, pageSize);

        model.addAttribute("clients", result.getClients());
        model.addAttribute("clientPage", result.getClientPage());
        model.addAttribute("searchQuery", result.getSearchQuery());
        model.addAttribute("searchType", searchTypeValue);
        model.addAttribute("pageNumbers", result.getPageNumbers());

        return "clients";
    }

    @GetMapping("/new")
    public String showClientForm(Model model) {
        model.addAttribute("client", new Client());
        return "client-form";
    }

    @PostMapping("/save")
    public String saveClient(@Valid @ModelAttribute Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "client-form";
        }

        clientService.saveClient(client);
        return "redirect:/clients";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Client client = clientService.getClientById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid client ID: " + id));
        model.addAttribute("client", client);
        return "client-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return "redirect:/clients";
    }
}