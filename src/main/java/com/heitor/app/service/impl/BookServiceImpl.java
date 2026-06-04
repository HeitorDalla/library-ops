package com.heitor.app.service.impl;

import com.heitor.app.dto.common.StockDTO;
import com.heitor.app.dto.input.BookCreateDTO;
import com.heitor.app.dto.input.BookPatchDTO;
import com.heitor.app.dto.input.BookUpdateDTO;
import com.heitor.app.dto.output.BookResponseDTO;
import com.heitor.app.entity.Book;
import com.heitor.app.enums.BookStatus;
import com.heitor.app.enums.LoanStatus;
import com.heitor.app.enums.RecordStatus;
import com.heitor.app.enums.ReservationStatus;
import com.heitor.app.exception.BookNotFoundException;
import com.heitor.app.exception.BusinessException;
import com.heitor.app.mapper.BookMapper;
import com.heitor.app.repository.BookRepository;
import com.heitor.app.repository.LoanRepository;
import com.heitor.app.repository.ReservationRepository;
import com.heitor.app.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final ReservationRepository reservationRepository;
    private final BookMapper mapper;

    public BookServiceImpl(BookRepository bookRepository,
                           LoanRepository loanRepository,
                           ReservationRepository reservationRepository,
                           BookMapper mapper) {
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
        this.reservationRepository = reservationRepository;
        this.mapper = mapper;
    }

    private List<Book> findAllBooks(String title,
                                   String author,
                                   String isbn,
                                   Long publicationYear,
                                   String language,
                                   Integer totalQuantity,
                                   BookStatus bookStatus,
                                   RecordStatus recordStatus) {
        return bookRepository.getAllBooks(
                title,
                author,
                isbn,
                publicationYear,
                language,
                totalQuantity,
                bookStatus,
                recordStatus
        );
    }

    private Book findBook (Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    private void validateBookCanBeDeactivated(Book book) {
        // Verificar se o livro esta em um empréstimo aberto ou atrasado
        if (loanRepository.existsByBookAndLoanStatus(book, List.of(LoanStatus.OPEN, LoanStatus.OVERDUE))) {
            throw new BusinessException("The book cannot be deactivated because they have active loans.");
        }

        // Verificar se o livro esta em uma reserva pendente ou expirada
        if (reservationRepository.existsByBookAndReservationStatus(book, List.of(ReservationStatus.PENDING, ReservationStatus.EXPIRED))) {
            throw new BusinessException("The book cannot be deactivated because they have active reservations.");
        }
    }

    @Override
    public List<BookResponseDTO> getAllBooks(String title,
                                             String author,
                                             String isbn,
                                             Long publicationYear,
                                             String language,
                                             Integer totalQuantity,
                                             BookStatus bookStatus,
                                             RecordStatus recordStatus) {

        List<Book> books = findAllBooks(title, author, isbn, publicationYear, language, totalQuantity, bookStatus, recordStatus);

        return mapper.toDtoList(books);
    }

    @Override
    public BookResponseDTO getBookById(Long id) {
        Book book = findBook(id);

        return mapper.toDto(book);
    }

    @Transactional
    @Override
    public BookResponseDTO createBook(BookCreateDTO dto){
        Book book = mapper.toEntity(dto);

        book.initialize(dto.getTotalQuantity());

        Book savedBook = bookRepository.save(book);
        return mapper.toDto(savedBook);
    }

    @Transactional
    @Override
    public BookResponseDTO partiallyUpdateBook(BookPatchDTO dto,
                                               Long id) {
        Book book = findBook(id);

        mapper.patchEntity(dto, book);

        bookRepository.save(book);
        return mapper.toDto(book);
    }

    @Transactional
    @Override
    public BookResponseDTO updateBook(BookUpdateDTO dto,
                                      Long id) {
        Book book = findBook(id);

        mapper.updateEntity(dto, book);

        bookRepository.save(book);
        return mapper.toDto(book);
    }

    // Métodos de Ativação e Desativação de Livros
    @Transactional
    @Override
    public void activateBook(Long id) {
        Book book = findBook(id);

        book.activate();

        bookRepository.save(book);
    }

    @Transactional
    @Override
    public void deactivateBook(Long id) {
        Book book = findBook(id);

        validateBookCanBeDeactivated(book);

        book.deactivate();

        bookRepository.save(book);
    }

    // Métodos de controle de estoque
    @Transactional
    @Override
    public BookResponseDTO addStock(StockDTO dto,
                                    Long id) {
        Book book = findBook(id);

        book.addStock(dto.getQuantity());

        bookRepository.save(book);
        return mapper.toDto(book);
    }

    @Transactional
    @Override
    public BookResponseDTO removeStock(StockDTO dto,
                                       Long id) {
        Book book = findBook(id);

        book.removeStock(dto.getQuantity());

        bookRepository.save(book);
        return mapper.toDto(book);
    }
}
