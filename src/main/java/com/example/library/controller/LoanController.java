package com.example.library.controller;

import com.example.library.dto.ActiveReaderDto;
import com.example.library.dto.LoanFormData;
import com.example.library.dto.LoanSearchResult;
import com.example.library.model.Loan;
import com.example.library.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    //    @GetMapping
//    public String listLoans(Model model) {
//        List<Loan> loans = loanService.getAllLoans();
//        model.addAttribute("loans", loans);
//        return "loans";
//    }
    @GetMapping
    public String listLoans(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("search") Optional<String> search,
            @RequestParam("searchType") Optional<String> searchType,
            Model model) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        String searchQuery = search.orElse("");
        String searchTypeValue = searchType.orElse("all");

        LoanSearchResult result = loanService.searchLoans(searchQuery, searchTypeValue, currentPage, pageSize);

        model.addAttribute("loans", result.getLoans());
        model.addAttribute("loanPage", result.getLoanPage());
        model.addAttribute("searchQuery", result.getSearchQuery());
        model.addAttribute("pageNumbers", result.getPageNumbers());
        model.addAttribute("searchType", searchTypeValue);

        return "loans";
    }


    //    @GetMapping("/new")
//    public String showLoanForm(Model model) {
//        model.addAttribute("loan", new Loan());
//        model.addAttribute("clients", loanService.getAvailableClients());
//        model.addAttribute("books", loanService.getAvailableBooks());
//        return "loan-form";
//    }
    @GetMapping("/new")
    public String showLoanForm(
            @RequestParam("clientQuery") Optional<String> clientQuery,
            @RequestParam("bookQuery") Optional<String> bookQuery,
            @RequestParam("selectedClientId") Optional<Long> selectedClientId,
            @RequestParam("selectedBookId") Optional<Long> selectedBookId,
            Model model) {

        model.addAttribute("loan", new Loan());
        model.addAttribute("clientQuery", clientQuery.orElse(""));
        model.addAttribute("bookQuery", bookQuery.orElse(""));

        LoanFormData formData = loanService.getLoanFormData(
                clientQuery.orElse(""),
                bookQuery.orElse(""),
                selectedClientId.orElse(null),
                selectedBookId.orElse(null)
        );

        model.addAttribute("selectedClient", formData.getSelectedClient());
        model.addAttribute("selectedBook", formData.getSelectedBook());
        model.addAttribute("clients", formData.getClients());
        model.addAttribute("books", formData.getBooks());
        model.addAttribute("selectedClientId", selectedClientId.orElse(null));
        model.addAttribute("selectedBookId", selectedBookId.orElse(null));

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

    //    @GetMapping("/active-readers")
//    @ResponseBody
//    public ResponseEntity<List<ActiveReaderDto>> getActiveReaders() {
//        List<ActiveReaderDto> result = loanService.getActiveReadersData();
//        return ResponseEntity.ok(result);
//    }
    @GetMapping("/active-readers")
    public String activeReadersReport(Model model) {
        List<ActiveReaderDto> activeReaders = loanService.getActiveReadersData();
        model.addAttribute("activeReaders", activeReaders);
        return "active-readers-report";
    }
}