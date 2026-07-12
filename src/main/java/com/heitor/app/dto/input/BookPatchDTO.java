package com.heitor.app.dto.input;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookPatchDTO {
    private String title;
    private String author;

    @Size(min = 10, max = 13)
    private String isbn;

    private Long publicationYear;
    private String language;
}
