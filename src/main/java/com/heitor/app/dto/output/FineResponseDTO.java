package com.heitor.app.dto.output;

import com.heitor.app.enums.FineStatus;
import com.heitor.app.enums.RecordStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FineResponseDTO {
    private Long id;
    private BigDecimal amount;
    private FineStatus fineStatus;
    private LocalDate createdDate;
    private LocalDate paymentDate;
    private Long loanId;
    private RecordStatus recordStatus;
}
