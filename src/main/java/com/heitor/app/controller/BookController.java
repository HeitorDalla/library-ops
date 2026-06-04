package com.heitor.app.controller;

import com.heitor.app.dto.common.StockDTO;
import com.heitor.app.dto.input.BookCreateDTO;
import com.heitor.app.dto.input.BookPatchDTO;
import com.heitor.app.dto.input.BookUpdateDTO;
import com.heitor.app.dto.output.BookResponseDTO;
import com.heitor.app.enums.BookStatus;
import com.heitor.app.enums.RecordStatus;
import com.heitor.app.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<BookResponseDTO>> getAllBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) Long publicationYear,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer totalQuantity,
            @RequestParam(required = false) BookStatus bookStatus,
            @RequestParam(required = false) RecordStatus recordStatus) {

        return ResponseEntity.ok(bookService.getAllBooks(
                title,
                author,
                isbn,
                publicationYear,
                language,
                totalQuantity,
                bookStatus,
                recordStatus
        ));
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BookResponseDTO> createBook(@Valid @RequestBody BookCreateDTO dto) {
        return ResponseEntity.ok(bookService.createBook(dto));
    }

    @PatchMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BookResponseDTO> partiallyUpdateBook(@Valid @RequestBody BookPatchDTO dto,
                                                               @PathVariable Long id) {
        return ResponseEntity.ok(bookService.partiallyUpdateBook(dto, id));
    }

    @PutMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BookResponseDTO> updateBook(@Valid @RequestBody BookUpdateDTO dto,
                                                      @PathVariable Long id) {
        return ResponseEntity.ok(bookService.updateBook(dto, id));
    }

    // Rotas para Ativar e Desativar Livros
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateBook(@PathVariable Long id) {
        bookService.deactivateBook(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateBook(@PathVariable Long id) {
        bookService.activateBook(id);

        return ResponseEntity.noContent().build();
    }

    // Rotas para controlar estoque
    @PatchMapping(
            path = "/{id}/add-stock",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BookResponseDTO> addStock(@Valid @RequestBody StockDTO dto,
                                                    @PathVariable Long id) {
        return ResponseEntity.ok(bookService.addStock(dto, id));
    }

    @PatchMapping(
            path = "/{id}/remove-stock",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BookResponseDTO> removeStock(@Valid @RequestBody StockDTO dto,
                                                       @PathVariable Long id) {
        return ResponseEntity.ok(bookService.removeStock(dto, id));
    }
}
