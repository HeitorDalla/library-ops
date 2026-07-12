package com.heitor.app.dto.output;

import com.heitor.app.enums.RecordStatus;
import com.heitor.app.enums.ReservationStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponseDTO {
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDate reservationDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private ReservationStatus reservationStatus;
    private RecordStatus recordStatus;
}
