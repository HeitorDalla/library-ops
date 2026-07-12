package com.heitor.app.dto.input;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanRequestDTO {
    @NotNull
    private Long userId;

    @NotEmpty
    private List<Long> bookIds;
}
