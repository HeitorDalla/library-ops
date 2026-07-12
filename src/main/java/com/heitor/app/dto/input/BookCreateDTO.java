package com.heitor.app.dto.input;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCreateDTO {
    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotBlank
    @Size(min = 10, max = 13)
    private String isbn;

    @NotNull
    private Long publicationYear;

    @NotBlank
    private String language;

    @NotNull
    @Min(1)
    private Integer totalQuantity;
}
