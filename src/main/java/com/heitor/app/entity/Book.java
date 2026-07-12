package com.heitor.app.entity;

import com.heitor.app.enums.BookStatus;
import com.heitor.app.enums.RecordStatus;
import com.heitor.app.exception.BusinessException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "book_title", nullable = false)
    private String title;

    @Column(name = "book_author", nullable = false, length = 150)
    private String author;

    @Column(name = "book_isbn", nullable = false, unique = true, length = 20)
    private String isbn;

    @Column(name = "book_publication_year", nullable = false)
    private Long publicationYear;

    @Column(name = "book_language", nullable = false)
    private String language;

    @Column(name = "book_total_quantity")
    private Integer totalQuantity;

    @Column(name = "book_available_quantity", nullable = false)
    private Integer availableQuantity;

    @Column(name = "book_registration_date", nullable = false)
    private LocalDate registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "book_status", nullable = false)
    private BookStatus bookStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "book_record_status", nullable = false)
    private RecordStatus recordStatus;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();

    @ManyToMany(mappedBy = "books", fetch = FetchType.LAZY)
    private List<Loan> loans = new ArrayList<>();

    public void activate() {
        bookStatus = BookStatus.AVAILABLE;
        recordStatus = RecordStatus.ACTIVE;
    }

    public void deactivate() {
        bookStatus = BookStatus.UNAVAILABLE;
        recordStatus = RecordStatus.INACTIVE;
    }

    private void validateActive() {
        if (recordStatus != RecordStatus.ACTIVE) {
            throw new BusinessException("Book is inactive.");
        }
    }

    public void initialize(Integer newTotalQuantity) {
        if (id != null) {
            throw new BusinessException("Book already initialized.");
        }

        if (newTotalQuantity == null || newTotalQuantity <= 0) {
            throw new BusinessException("Total quantity must be greater than zero.");
        }

        registrationDate = LocalDate.now();
        this.totalQuantity = newTotalQuantity;
        availableQuantity = totalQuantity;
        activate();
    }

    /* Controle de Estoque */

    // Método para administradores adicionar livros ao estoque
    public void addStock(Integer quantity) {
        validateActive();

        if (quantity <= 0) {
            throw new BusinessException("Quantity must be greater than zero.");
        }

        totalQuantity += quantity;
        availableQuantity += quantity;
    }

    // Método para administradores remover livros ao estoque
    public void removeStock(Integer quantity) {
        if (quantity <= 0) {
            throw new BusinessException("Quantity must be greater than zero.");
        }

        if (availableQuantity < quantity) {
            throw new BusinessException("Out of stock.");
        }

        totalQuantity -= quantity;
        availableQuantity -= quantity;
    }

    /* Empréstimo */

    // Método para emprestimo de livro com controle de estoque
    public void borrow() {
        validateActive();

        if (availableQuantity <= 0) {
            throw new BusinessException("Book out of stock.");
        }

        availableQuantity--;

        if (availableQuantity == 0) {
            bookStatus = BookStatus.UNAVAILABLE;
        }
    }

    // Método para retorno de livros do emprestimo com controle de estoque
    public void returnBook() {
        validateActive();

        availableQuantity++;

        if (availableQuantity > 0) {
            bookStatus = BookStatus.AVAILABLE;
        }
    }

    /* Reserva */
    public void reserve() {
        validateActive();

        if (availableQuantity <= 0) {
            throw new BusinessException("No available copies for reservation.");
        }

        availableQuantity--;

        if (availableQuantity == 0) {
            bookStatus = BookStatus.RESERVED;
        }
    }

    public void releaseReservation() {
        validateActive();

        if (availableQuantity < totalQuantity) {
            availableQuantity++;
        }

        bookStatus = BookStatus.AVAILABLE;
    }
}