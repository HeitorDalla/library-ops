package com.heitor.app.dto.output;

import com.heitor.app.enums.LoanStatus;
import com.heitor.app.enums.RecordStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanResponseDTO {
    private Long id;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private LoanStatus loanStatus;
    private RecordStatus recordStatus;
    private Long userId;
    private List<Long> booksId;
    private Boolean hasFine;
}
