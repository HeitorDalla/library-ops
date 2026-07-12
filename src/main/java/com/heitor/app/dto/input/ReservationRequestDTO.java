package com.heitor.app.dto.input;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequestDTO {
    private Long userId;
    private Long bookId;
}
