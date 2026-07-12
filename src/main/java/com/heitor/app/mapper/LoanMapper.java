package com.heitor.app.mapper;

import com.heitor.app.dto.output.LoanResponseDTO;
import com.heitor.app.entity.Book;
import com.heitor.app.entity.Loan;
import com.heitor.app.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoanMapper {

    public List<LoanResponseDTO> toDtoList(List<Loan> loans) {
        if (loans.isEmpty()) {
            return List.of();
        }

        return loans.stream()
                .map(this::toDto)
                .toList();
    }

    public LoanResponseDTO toDto(Loan loan) {
        if (loan == null) {
            return null;
        }

        return LoanResponseDTO.builder()
                .id(loan.getId())
                .loanDate(loan.getLoanDate())
                .dueDate(loan.getDueDate())
                .returnDate(loan.getReturnDate())
                .loanStatus(loan.getLoanStatus())
                .recordStatus(loan.getRecordStatus())
                .userId(loan.getUser().getId())
                .booksId(
                        loan.getBooks().stream()
                                .map(Book::getId)
                                .toList()
                )
                .hasFine(loan.getFine() != null)
                .build();
    }

    public Loan toEntity(User user, List<Book> books) {
        return Loan.builder()
                .user(user)
                .books(books)
                .build();
    }
}
