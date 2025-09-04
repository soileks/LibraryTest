package com.example.library.mapper;

import com.example.library.dto.ActiveReaderDto;
import com.example.library.model.Loan;


public class LoanMapper {

    public static ActiveReaderDto convertLoanToDto(Loan loan) {
        if (loan == null) return null;

        ActiveReaderDto dto = new ActiveReaderDto();
        if (loan.getClient() != null) {
            dto.setFullName(loan.getClient().getFullName());
            dto.setBirthDate(loan.getClient().getBirthDate());
        }
        if (loan.getBook() != null) {
            dto.setBookTitle(loan.getBook().getTitle());
            dto.setBookAuthor(loan.getBook().getAuthor());
            dto.setBookIsbn(loan.getBook().getIsbn());
        }
        dto.setLoanDate(loan.getLoanDate());
        return dto;
    }

}
