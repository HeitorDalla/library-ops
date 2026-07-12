package com.heitor.app.entity;

import com.heitor.app.enums.FineStatus;
import com.heitor.app.enums.RecordStatus;
import com.heitor.app.exception.BusinessException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "fines")
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @Column(name = "fine_amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "fine_created_date", nullable = false)
    private LocalDate createdDate;

    @Column(name = "fine_payment_date")
    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "fine_status", nullable = false)
    private FineStatus fineStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "fine_record_status", nullable = false)
    private RecordStatus recordStatus;

    public void pay() {
        if (fineStatus != FineStatus.OPEN) {
            throw new BusinessException("Only open fines can be paid.");
        }

        paymentDate = LocalDate.now();
        fineStatus = FineStatus.PAID;
        recordStatus = RecordStatus.INACTIVE;
    }

    public void cancel() {
        if (fineStatus != FineStatus.OPEN) {
            throw new BusinessException("Only open fines can be cancelled.");
        }

        fineStatus = FineStatus.CANCELLED;
        recordStatus = RecordStatus.INACTIVE;
    }
}