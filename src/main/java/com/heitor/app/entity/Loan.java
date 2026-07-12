package com.heitor.app.entity;

import com.heitor.app.enums.LoanStatus;
import com.heitor.app.enums.RecordStatus;
import com.heitor.app.exception.BusinessException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "loan_books",
            joinColumns = @JoinColumn(name = "loan_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> books = new ArrayList<>();

    @Column(name = "loan_date", nullable = false)
    private LocalDate loanDate;

    @Column(name = "loan_due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "loan_return_date")
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_status", nullable = false)
    private LoanStatus loanStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_record_status", nullable = false)
    private RecordStatus recordStatus;

    @OneToOne(mappedBy = "loan")
    private Fine fine;

    public void initialize() {
        loanDate = LocalDate.now();
        dueDate = loanDate.plusWeeks(1);
        loanStatus = LoanStatus.OPEN;
        recordStatus = RecordStatus.ACTIVE;
    }

    public void validateCanBeReturned() {
        if (loanStatus == LoanStatus.RETURNED) {
            throw new BusinessException("The loan has already been returned.");
        }

        if (loanStatus == LoanStatus.CANCELLED) {
            throw new BusinessException("Cancelled loan cannot be returned.");
        }
    }

    public void finish() {
        returnDate = LocalDate.now();
        loanStatus = LoanStatus.RETURNED;
        recordStatus = RecordStatus.INACTIVE;
    }

    public void cancel() {
        if (loanStatus != LoanStatus.OPEN) {
            throw new BusinessException("Only open loans can be cancelled.");
        }

        loanStatus = LoanStatus.CANCELLED;
        recordStatus = RecordStatus.INACTIVE;
    }

    public void markAsOverdue() {
        if (loanStatus != LoanStatus.OPEN) {
            return;
        }

        loanStatus = LoanStatus.OVERDUE;
    }
}