package com.heitor.app.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpsertDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String number;

    @Email
    @NotBlank
    private String email;
}
