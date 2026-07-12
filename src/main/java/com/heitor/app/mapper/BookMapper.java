package com.heitor.app.mapper;

import com.heitor.app.dto.input.BookCreateDTO;
import com.heitor.app.dto.input.BookPatchDTO;
import com.heitor.app.dto.input.BookUpdateDTO;
import com.heitor.app.dto.output.BookResponseDTO;
import com.heitor.app.entity.Book;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookMapper {
    public List<BookResponseDTO> toDtoList(List<Book> entities) {
        if (entities.isEmpty()) {
            return List.of();
        }

        return entities.stream()
                .map(this::toDto)
                .toList();
    }

    public BookResponseDTO toDto(Book entity) {
        if (entity == null) {
            return null;
        }

        return BookResponseDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .author(entity.getAuthor())
                .isbn(entity.getIsbn())
                .publicationYear(entity.getPublicationYear())
                .language(entity.getLanguage())
                .totalQuantity(entity.getTotalQuantity())
                .registrationDate(entity.getRegistrationDate())
                .availableQuantity(entity.getAvailableQuantity())
                .bookStatus(entity.getBookStatus())
                .recordStatus(entity.getRecordStatus())
                .build();
    }

    public Book toEntity(BookCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        return Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .isbn(dto.getIsbn())
                .publicationYear(dto.getPublicationYear())
                .language(dto.getLanguage())
                .totalQuantity(dto.getTotalQuantity())
                .build();
    }

    public void patchEntity(BookPatchDTO dto, Book entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle());
        }

        if (dto.getAuthor() != null) {
            entity.setAuthor(dto.getAuthor());
        }

        if (dto.getIsbn() != null) {
            entity.setIsbn(dto.getIsbn());
        }

        if (dto.getPublicationYear() != null) {
            entity.setPublicationYear(dto.getPublicationYear());
        }

        if (dto.getLanguage() != null) {
            entity.setLanguage(dto.getLanguage());
        }
    }

    public void updateEntity(BookUpdateDTO dto, Book entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setTitle(dto.getTitle());
        entity.setAuthor(dto.getAuthor());
        entity.setIsbn(dto.getIsbn());
        entity.setPublicationYear(dto.getPublicationYear());
        entity.setLanguage(dto.getLanguage());
    }
}
