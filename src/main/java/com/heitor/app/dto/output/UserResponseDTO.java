package com.heitor.app.dto.output;

import com.heitor.app.enums.RecordStatus;
import com.heitor.app.enums.UserStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private Long id;
    private String name;
    private String number;
    private String email;
    private LocalDate registrationDate;
    private UserStatus userStatus;
    private RecordStatus recordStatus;
}
