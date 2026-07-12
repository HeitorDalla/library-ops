package com.heitor.app.dto.output;

import com.heitor.app.enums.BookStatus;
import com.heitor.app.enums.RecordStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDTO {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Long publicationYear;
    private String language;
    private Integer totalQuantity;
    private Integer availableQuantity;
    private LocalDate registrationDate;
    private BookStatus bookStatus;
    private RecordStatus recordStatus;
}
