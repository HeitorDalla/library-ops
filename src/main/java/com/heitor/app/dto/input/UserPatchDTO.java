package com.heitor.app.dto.input;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPatchDTO {
    private String name;
    private String number;

    @Email
    private String email;
}
