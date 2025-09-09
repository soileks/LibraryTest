package com.example.library.controller;

import com.example.library.dto.ActiveReaderDto;
import com.example.library.dto.LoanFormData;
import com.example.library.dto.LoanSearchResult;
import com.example.library.model.Loan;
import com.example.library.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping
    public String listLoans(
            @RequestParam(value = "page", defaultValue = "1") @Min(1) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(value = "search", defaultValue = "") String searchQuery,
            @RequestParam(value = "searchType", defaultValue = "all") String searchType,
            Model model) {

        LoanSearchResult result = loanService.searchLoans(searchQuery, searchType, page, size);

        model.addAttribute("loans", result.getLoans());
        model.addAttribute("loanPage", result.getLoanPage());
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("pageNumbers", result.getPageNumbers());
        model.addAttribute("searchType", searchType);

        return "loans";
    }

    @GetMapping("/new")
    public String showLoanForm(
            @RequestParam(value = "clientQuery", defaultValue = "") String clientQuery,
            @RequestParam(value = "clientSearchType", defaultValue = "all") String clientSearchType,
            @RequestParam(value = "bookQuery", defaultValue = "") String bookQuery,
            @RequestParam(value = "bookSearchType", defaultValue = "all") String bookSearchType,
            @RequestParam(value = "selectedClientId", required = false) Long selectedClientId,
            @RequestParam(value = "selectedBookId", required = false) Long selectedBookId,
            Model model) {

        model.addAttribute("clientQuery", clientQuery);
        model.addAttribute("clientSearchType", clientSearchType);
        model.addAttribute("bookQuery", bookQuery);
        model.addAttribute("bookSearchType", bookSearchType);

        LoanFormData formData = loanService.getLoanFormData(
                clientQuery,
                clientSearchType,
                bookQuery,
                bookSearchType,
                selectedClientId,
                selectedBookId
        );

        model.addAttribute("selectedClient", formData.getSelectedClient());
        model.addAttribute("selectedBook", formData.getSelectedBook());
        model.addAttribute("clients", formData.getClients());
        model.addAttribute("books", formData.getBooks());
        model.addAttribute("selectedClientId", selectedClientId);
        model.addAttribute("selectedBookId", selectedBookId);

        return "loan-form";
    }

    @PostMapping("/save")
    public String saveLoan(
            @RequestParam Long selectedClientId,
            @RequestParam Long selectedBookId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate loanDate) {

        loanService.createLoan(selectedClientId, selectedBookId, loanDate);
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
    public ResponseEntity<List<ActiveReaderDto>> getActiveReaders() {
        List<ActiveReaderDto> result = loanService.getActiveReadersData();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/active-readers-report")
    public String activeReadersReport(Model model) {
        List<ActiveReaderDto> activeReaders = loanService.getActiveReadersData();
        model.addAttribute("activeReaders", activeReaders);
        return "active-readers-report";
    }
}