package com.example.library.dto;

import com.example.library.model.Loan;
import org.springframework.data.domain.Page;

import java.util.List;

public class LoanSearchResult {
    private List<Loan> loans;
    private List<Integer> pageNumbers;
    private String searchQuery;
    private Page<Loan> loanPage;

    public LoanSearchResult(List<Loan> loans, Page<Loan> loanPage,
                            List<Integer> pageNumbers, String searchQuery) {
        this.loans = loans;
        this.loanPage = loanPage;
        this.pageNumbers = pageNumbers;
        this.searchQuery = searchQuery;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public List<Integer> getPageNumbers() {
        return pageNumbers;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public Page<Loan> getLoanPage() {
        return loanPage;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

    public void setPageNumbers(List<Integer> pageNumbers) {
        this.pageNumbers = pageNumbers;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public void setLoanPage(Page<Loan> loanPage) {
        this.loanPage = loanPage;
    }
}
