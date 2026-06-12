package com.heitor.app.web.controller;

import com.heitor.app.controller.BookController;
import com.heitor.app.dto.common.StockDTO;
import com.heitor.app.dto.input.*;
import com.heitor.app.dto.output.BookResponseDTO;
import com.heitor.app.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookControllerWebTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookService bookService;

    @Test
    public void deveCriarLivroERetornarCreated() {
        BookCreateDTO bookCreateDTO = new BookCreateDTO();
        bookCreateDTO.setTitle("Titulo teste");
        bookCreateDTO.setAuthor("Author teste");
        bookCreateDTO.setIsbn("123456789");
        bookCreateDTO.setPublicationYear(2000L);
        bookCreateDTO.setLanguage("Portugues");
        bookCreateDTO.setTotalQuantity(20);

        BookResponseDTO bookResponseDTO = new BookResponseDTO();
        bookResponseDTO.setTitle("Titulo teste");
        bookResponseDTO.setAuthor("Author teste");
        bookResponseDTO.setIsbn("123456789");
        bookResponseDTO.setPublicationYear(2000L);
        bookResponseDTO.setLanguage("Portugues");
        bookResponseDTO.setTotalQuantity(20);

        when(bookService.createBook(bookCreateDTO))
                .thenReturn(bookResponseDTO);

        ResponseEntity<BookResponseDTO> resultado = bookController.createBook(bookCreateDTO);

        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals("Titulo teste", resultado.getBody().getTitle());
        assertEquals("Author teste", resultado.getBody().getAuthor());
        assertEquals("123456789", resultado.getBody().getIsbn());
        assertEquals(2000L, resultado.getBody().getPublicationYear());
        assertEquals("Portugues", resultado.getBody().getLanguage());
        assertEquals(20, resultado.getBody().getTotalQuantity());

        verify(bookService).createBook(bookCreateDTO);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void deveAtualizarParcialmenteLivroERetornarOk() {
        Long idBook = 1L;

        BookPatchDTO bookPatchDTO = new BookPatchDTO();
        bookPatchDTO.setTitle("Titulo teste");
        bookPatchDTO.setAuthor("Author teste");
        bookPatchDTO.setIsbn("123456789");
        bookPatchDTO.setPublicationYear(2000L);
        bookPatchDTO.setLanguage("Portugues");

        BookResponseDTO bookResponseDTO = new BookResponseDTO();
        bookResponseDTO.setTitle("Titulo teste");
        bookResponseDTO.setAuthor("Author teste");
        bookResponseDTO.setIsbn("123456789");
        bookResponseDTO.setPublicationYear(2000L);
        bookResponseDTO.setLanguage("Portugues");

        when(bookService.partiallyUpdateBook(bookPatchDTO, idBook))
                .thenReturn(bookResponseDTO);

        ResponseEntity<BookResponseDTO> resultado = bookController.partiallyUpdateBook(bookPatchDTO, idBook);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals("Titulo teste", resultado.getBody().getTitle());
        assertEquals("Author teste", resultado.getBody().getAuthor());
        assertEquals("123456789", resultado.getBody().getIsbn());
        assertEquals(2000L, resultado.getBody().getPublicationYear());
        assertEquals("Portugues", resultado.getBody().getLanguage());

        verify(bookService).partiallyUpdateBook(bookPatchDTO, idBook);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void deveAtualizarLivroERetornarOk() {
        Long idBook = 1L;

        BookUpdateDTO bookUpdateDTO = new BookUpdateDTO();
        bookUpdateDTO.setTitle("Titulo teste");
        bookUpdateDTO.setAuthor("Author teste");
        bookUpdateDTO.setIsbn("123456789");
        bookUpdateDTO.setPublicationYear(2000L);
        bookUpdateDTO.setLanguage("Portugues");

        BookResponseDTO bookResponseDTO = new BookResponseDTO();
        bookResponseDTO.setTitle("Titulo teste");
        bookResponseDTO.setAuthor("Author teste");
        bookResponseDTO.setIsbn("123456789");
        bookResponseDTO.setPublicationYear(2000L);
        bookResponseDTO.setLanguage("Portugues");

        when(bookService.updateBook(bookUpdateDTO, idBook))
                .thenReturn(bookResponseDTO);

        ResponseEntity<BookResponseDTO> resultado = bookController.updateBook(bookUpdateDTO, idBook);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals("Titulo teste", resultado.getBody().getTitle());
        assertEquals("Author teste", resultado.getBody().getAuthor());
        assertEquals("123456789", resultado.getBody().getIsbn());
        assertEquals(2000L, resultado.getBody().getPublicationYear());
        assertEquals("Portugues", resultado.getBody().getLanguage());

        verify(bookService).updateBook(bookUpdateDTO, idBook);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void deveDesativarLivroERetornarNoContent() {
        Long idBook = 1L;

        doNothing().when(bookService).deactivateBook(idBook);

        ResponseEntity<Void> resultado = bookController.deactivateBook(idBook);

        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
        assertNull(resultado.getBody());

        verify(bookService).deactivateBook(idBook);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void deveAtivarLivroERetornarNoContent() {
        Long idBook = 1L;

        doNothing().when(bookService).activateBook(idBook);

        ResponseEntity<Void> resultado = bookController.activateBook(idBook);

        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
        assertNull(resultado.getBody());

        verify(bookService).activateBook(idBook);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void deveAdicionarEstoqueERetornarOk() {
        StockDTO stockDTO = new StockDTO();
        stockDTO.setQuantity(10);

        Long idBook = 1L;

        BookResponseDTO bookResponseDTO = new BookResponseDTO();
        bookResponseDTO.setTitle("Titulo teste");
        bookResponseDTO.setAuthor("Author teste");
        bookResponseDTO.setIsbn("123456789");
        bookResponseDTO.setPublicationYear(2000L);
        bookResponseDTO.setLanguage("Portugues");

        when(bookService.addStock(stockDTO, idBook))
                .thenReturn(bookResponseDTO);

        ResponseEntity<BookResponseDTO> resultado = bookController.addStock(stockDTO, idBook);

        assertNotNull(resultado.getBody());
        assertEquals(HttpStatus.OK, resultado.getStatusCode());

        verify(bookService).addStock(stockDTO, idBook);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void deveRemoverEstoqueERetornarOk() {
        StockDTO stockDTO = new StockDTO();
        stockDTO.setQuantity(10);

        Long idBook = 1L;

        BookResponseDTO bookResponseDTO = new BookResponseDTO();
        bookResponseDTO.setTitle("Titulo teste");
        bookResponseDTO.setAuthor("Author teste");
        bookResponseDTO.setIsbn("123456789");
        bookResponseDTO.setPublicationYear(2000L);
        bookResponseDTO.setLanguage("Portugues");

        when(bookService.removeStock(stockDTO, idBook))
                .thenReturn(bookResponseDTO);

        ResponseEntity<BookResponseDTO> resultado = bookController.removeStock(stockDTO, idBook);

        assertNotNull(resultado.getBody());
        assertEquals(HttpStatus.OK, resultado.getStatusCode());

        verify(bookService).removeStock(stockDTO, idBook);
        verifyNoMoreInteractions(bookService);
    }
}