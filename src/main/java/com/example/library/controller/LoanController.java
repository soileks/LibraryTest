package com.example.library.controller;


import com.example.library.model.Loan;

import com.example.library.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping
    public String listLoans(Model model) {
        List<Loan> loans = loanService.getAllLoans();
        model.addAttribute("loans", loans);
        return "loans";
    }


    @GetMapping("/new")
    public String showLoanForm(Model model) {
        model.addAttribute("loan", new Loan());
        model.addAttribute("clients", loanService.getAvailableClients());
        model.addAttribute("books", loanService.getAvailableBooks());
        return "loan-form";
    }

    @PostMapping("/save")
    public String saveLoan(@RequestParam Long clientId,
                           @RequestParam Long bookId,
                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate loanDate) {

        loanService.createLoan(clientId, bookId, loanDate);

        return "redirect:/loans";
    }

    @GetMapping("/delete/{id}")
    public String deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return "redirect:/loans";
    }
    @GetMapping("/return/{id}")
    public String returnBook(@PathVariable Long id) {
        loanService.returnBook(id);
        return "redirect:/loans";
    }

    @GetMapping("/active-readers")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getActiveReaders() {
        List<Map<String, Object>> result = loanService.getActiveReadersData();
        return ResponseEntity.ok(result);
    }
}