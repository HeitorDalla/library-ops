package com.heitor.app.service.impl;

import com.heitor.app.dto.common.StockDTO;
import com.heitor.app.dto.input.BookCreateDTO;
import com.heitor.app.dto.input.BookPatchDTO;
import com.heitor.app.dto.input.BookUpdateDTO;
import com.heitor.app.dto.output.BookResponseDTO;
import com.heitor.app.entity.Book;
import com.heitor.app.enums.*;
import com.heitor.app.exception.BookNotFoundException;
import com.heitor.app.exception.BusinessException;
import com.heitor.app.mapper.BookMapper;
import com.heitor.app.repository.BookRepository;
import com.heitor.app.repository.LoanRepository;
import com.heitor.app.repository.ReservationRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private BookMapper bookMapper;

    @Test
    public void deveCriarLivroQuandoDadosValidos() {
        BookCreateDTO dto = new BookCreateDTO();
        dto.setTotalQuantity(10);

        Book book = new Book();

        Book savedBook = new Book();
        savedBook.setId(1L);

        BookResponseDTO responseDTO = new BookResponseDTO();
        responseDTO.setId(1L);

        when(bookMapper.toEntity(dto))
                .thenReturn(book);
        when(bookRepository.save(book))
                .thenReturn(savedBook);
        when(bookMapper.toDto(savedBook))
                .thenReturn(responseDTO);

        BookResponseDTO result = bookServiceImpl.createBook(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(10, book.getTotalQuantity());
        assertEquals(10, book.getAvailableQuantity());
        assertEquals(BookStatus.AVAILABLE, book.getBookStatus());
        assertEquals(RecordStatus.ACTIVE, book.getRecordStatus());

        verify(bookMapper)
                .toEntity(dto);
        verify(bookRepository)
                .save(book);
        verify(bookMapper)
                .toDto(savedBook);
    }

    @Test
    public void deveAtualizarParcialmenteLivroQuandoLivroExiste() {
        Long idBook = 1L;

        Book book = new Book();
        book.setId(idBook);

        BookPatchDTO dto = new BookPatchDTO();

        BookResponseDTO responseDTO = new BookResponseDTO();
        responseDTO.setId(idBook);

        when(bookRepository.findById(idBook))
                .thenReturn(Optional.of(book));
        when(bookMapper.toDto(book))
                .thenReturn(responseDTO);

        BookResponseDTO result = bookServiceImpl.partiallyUpdateBook(dto, idBook);

        assertNotNull(result);
        assertEquals(idBook, result.getId());

        verify(bookRepository)
                .findById(idBook);
        verify(bookMapper)
                .patchEntity(dto, book);
        verify(bookRepository)
                .save(book);
        verify(bookMapper)
                .toDto(book);
    }

    @Test
    public void deveLancarExcecaoQuandoAtualizarParcialmenteLivroInexistente() {
        Long idBook = 1L;

        BookPatchDTO dto = new BookPatchDTO();

        when(bookRepository.findById(idBook))
                .thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> bookServiceImpl.partiallyUpdateBook(dto, idBook));

        verify(bookRepository)
                .findById(idBook);
        verify(bookMapper, never())
                .patchEntity(any(), any());
        verify(bookRepository, never())
                .save(any());
    }

    @Test
    public void deveAtualizarLivroQuandoLivroExiste() {
        Long idBook = 1L;

        Book book = new Book();
        book.setId(idBook);

        BookUpdateDTO dto = new BookUpdateDTO();

        BookResponseDTO responseDTO = new BookResponseDTO();
        responseDTO.setId(idBook);

        when(bookRepository.findById(idBook))
                .thenReturn(Optional.of(book));
        when(bookMapper.toDto(book))
                .thenReturn(responseDTO);

        BookResponseDTO result = bookServiceImpl.updateBook(dto, idBook);

        assertNotNull(result);
        assertEquals(idBook, result.getId());

        verify(bookRepository)
                .findById(idBook);
        verify(bookMapper)
                .updateEntity(dto, book);
        verify(bookRepository)
                .save(book);
        verify(bookMapper)
                .toDto(book);
    }

    @Test
    public void deveLancarExcecaoQuandoAtualizarLivroInexistente() {
        Long idBook = 1L;

        BookUpdateDTO dto = new BookUpdateDTO();

        when(bookRepository.findById(idBook))
                .thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> bookServiceImpl.updateBook(dto, idBook));

        verify(bookRepository)
                .findById(idBook);
        verify(bookMapper, never())
                .updateEntity(any(), any());
        verify(bookRepository, never())
                .save(any());
    }

    @Test
    public void deveAtivarLivroQuandoLivroExiste() {
        Long idBook = 1L;

        Book book = new Book();
        book.setId(idBook);
        book.deactivate();

        when(bookRepository.findById(idBook))
                .thenReturn(Optional.of(book));

        bookServiceImpl.activateBook(idBook);

        assertEquals(BookStatus.AVAILABLE, book.getBookStatus());
        assertEquals(RecordStatus.ACTIVE, book.getRecordStatus());

        verify(bookRepository)
                .findById(idBook);
        verify(bookRepository)
                .save(book);
    }

    @Test
    public void deveLancarExcecaoQuandoAtivarLivroInexistente() {
        Long idBook = 1L;

        when(bookRepository.findById(idBook))
                .thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> bookServiceImpl.activateBook(idBook));

        verify(bookRepository)
                .findById(idBook);
        verify(bookRepository, never())
                .save(any());
    }

    @Test
    public void deveDesativarLivroQuandoNaoPossuiPendencias() {
        Long idBook = 1L;

        Book book = new Book();
        book.setId(idBook);
        book.activate();

        when(bookRepository.findById(idBook))
                .thenReturn(Optional.of(book));
        when(loanRepository.existsByBookAndLoanStatus(
                book,
                List.of(LoanStatus.OPEN, LoanStatus.OVERDUE)
        )).thenReturn(false);
        when(reservationRepository.existsByBookAndReservationStatus(
                book,
                List.of(ReservationStatus.PENDING, ReservationStatus.EXPIRED)
        )).thenReturn(false);

        bookServiceImpl.deactivateBook(idBook);

        assertEquals(BookStatus.UNAVAILABLE, book.getBookStatus());
        assertEquals(RecordStatus.INACTIVE, book.getRecordStatus());

        verify(bookRepository)
                .findById(idBook);
        verify(loanRepository)
                .existsByBookAndLoanStatus(
                        book,
                        List.of(LoanStatus.OPEN, LoanStatus.OVERDUE)
                );
        verify(reservationRepository)
                .existsByBookAndReservationStatus(
                        book,
                        List.of(ReservationStatus.PENDING, ReservationStatus.EXPIRED)
                );
        verify(bookRepository)
                .save(book);
    }

    @Test
    public void naoDeveDesativarLivroQuandoPossuiEmprestimosAtivos() {
        Long idBook = 1L;

        Book book = new Book();

        when(bookRepository.findById(idBook))
                .thenReturn(Optional.of(book));
        when(loanRepository.existsByBookAndLoanStatus(
                book,
                List.of(LoanStatus.OPEN, LoanStatus.OVERDUE)
        )).thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> bookServiceImpl.deactivateBook(idBook)
        );

        assertEquals(
                "The book cannot be deactivated because they have active loans.",
                exception.getMessage()
        );

        verify(bookRepository)
                .findById(idBook);
        verify(reservationRepository, never())
                .existsByBookAndReservationStatus(any(), any());
        verify(bookRepository, never())
                .save(any());
    }

    @Test
    public void naoDeveDesativarLivroQuandoPossuiReservasAtivas() {
        Long idBook = 1L;

        Book book = new Book();

        when(bookRepository.findById(idBook))
                .thenReturn(Optional.of(book));

        when(loanRepository.existsByBookAndLoanStatus(
                book,
                List.of(LoanStatus.OPEN, LoanStatus.OVERDUE)
        )).thenReturn(false);
        when(reservationRepository.existsByBookAndReservationStatus(
                book,
                List.of(ReservationStatus.PENDING, ReservationStatus.EXPIRED)
        )).thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> bookServiceImpl.deactivateBook(idBook)
        );

        assertEquals(
                "The book cannot be deactivated because they have active reservations.",
                exception.getMessage()
        );

        verify(bookRepository)
                .findById(idBook);
        verify(bookRepository, never())
                .save(any());
    }

    @Test
    public void deveLancarExcecaoQuandoDesativarLivroInexistente() {
        Long idBook = 1L;

        when(bookRepository.findById(idBook))
                .thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> bookServiceImpl.deactivateBook(idBook));

        verify(bookRepository)
                .findById(idBook);
        verify(bookRepository, never())
                .save(any());
    }

    @Test
    public void deveAdicionarEstoqueQuandoLivroExiste() {
        Long idBook = 1L;

        Book book = new Book();
        book.initialize(10);

        StockDTO dto = new StockDTO();
        dto.setQuantity(5);

        BookResponseDTO responseDTO = new BookResponseDTO();
        responseDTO.setId(idBook);

        when(bookRepository.findById(idBook))
                .thenReturn(Optional.of(book));
        when(bookMapper.toDto(book))
                .thenReturn(responseDTO);

        BookResponseDTO result = bookServiceImpl.addStock(dto, idBook);

        assertNotNull(result);
        assertEquals(15, book.getTotalQuantity());
        assertEquals(15, book.getAvailableQuantity());

        verify(bookRepository)
                .findById(idBook);
        verify(bookRepository)
                .save(book);
        verify(bookMapper)
                .toDto(book);
    }

    @Test
    public void deveLancarExcecaoQuandoAdicionarEstoqueLivroInexistente() {
        Long idBook = 1L;

        StockDTO dto = new StockDTO();
        dto.setQuantity(5);

        when(bookRepository.findById(idBook))
                .thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> bookServiceImpl.addStock(dto, idBook));

        verify(bookRepository, never())
                .save(any());
    }

    @Test
    public void deveRemoverEstoqueQuandoLivroExiste() {
        Long idBook = 1L;

        Book book = new Book();
        book.initialize(10);

        StockDTO dto = new StockDTO();
        dto.setQuantity(5);

        BookResponseDTO responseDTO = new BookResponseDTO();
        responseDTO.setId(idBook);

        when(bookRepository.findById(idBook))
                .thenReturn(Optional.of(book));
        when(bookMapper.toDto(book))
                .thenReturn(responseDTO);

        BookResponseDTO result = bookServiceImpl.removeStock(dto, idBook);

        assertNotNull(result);
        assertEquals(5, book.getTotalQuantity());
        assertEquals(5, book.getAvailableQuantity());

        verify(bookRepository)
                .findById(idBook);
        verify(bookRepository)
                .save(book);
        verify(bookMapper)
                .toDto(book);
    }

    @Test
    public void deveLancarExcecaoQuandoRemoverEstoqueLivroInexistente() {
        Long idBook = 1L;

        StockDTO dto = new StockDTO();
        dto.setQuantity(5);

        when(bookRepository.findById(idBook))
                .thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> bookServiceImpl.removeStock(dto, idBook));

        verify(bookRepository, never())
                .save(any());
    }
}