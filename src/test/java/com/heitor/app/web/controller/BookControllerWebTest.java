package com.heitor.app.web.controller;

import com.heitor.app.controller.BookController;
import com.heitor.app.dto.common.StockDTO;
import com.heitor.app.dto.input.BookCreateDTO;
import com.heitor.app.dto.input.BookPatchDTO;
import com.heitor.app.dto.input.BookUpdateDTO;
import com.heitor.app.dto.output.BookResponseDTO;
import com.heitor.app.enums.BookStatus;
import com.heitor.app.enums.RecordStatus;
import com.heitor.app.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    private BookResponseDTO criarBookResponseDTO() {
        return BookResponseDTO.builder()
                .id(1L)
                .title("Livro teste")
                .author("Autor teste")
                .isbn("9788535902778")
                .publicationYear(2024L)
                .language("Português")
                .totalQuantity(10)
                .availableQuantity(8)
                .registrationDate(LocalDate.of(2024, 1, 10))
                .bookStatus(BookStatus.AVAILABLE)
                .recordStatus(RecordStatus.ACTIVE)
                .build();
    }

    // Testes para o metodo 'getAllBooks'
    @Test
    public void getAllBooks_deveRetornarOk_quandoNaoHouverFiltros() throws Exception {
        BookResponseDTO bookResponseDTO = criarBookResponseDTO();

        List<BookResponseDTO> books = List.of(bookResponseDTO);

        when(bookService.getAllBooks(null, null, null, null, null, null, null, null))
                .thenReturn(books);

        mockMvc.perform(get("/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Livro teste"))
                .andExpect(jsonPath("$[0].author").value("Autor teste"))
                .andExpect(jsonPath("$[0].isbn").value("9788535902778"))
                .andExpect(jsonPath("$[0].publicationYear").value(2024))
                .andExpect(jsonPath("$[0].language").value("Português"))
                .andExpect(jsonPath("$[0].totalQuantity").value(10))
                .andExpect(jsonPath("$[0].availableQuantity").value(8))
                .andExpect(jsonPath("$[0].registrationDate").value("2024-01-10"))
                .andExpect(jsonPath("$[0].bookStatus").value("AVAILABLE"))
                .andExpect(jsonPath("$[0].recordStatus").value("ACTIVE"));

        verify(bookService, times(1)).getAllBooks(null, null, null, null, null, null, null, null);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void getAllBooks_deveRetornarOk_quandoTituloForInformado() throws Exception {
        BookResponseDTO bookResponseDTO = criarBookResponseDTO();

        List<BookResponseDTO> books = List.of(bookResponseDTO);

        when(bookService.getAllBooks("Livro teste", null, null, null, null, null, null, null))
                .thenReturn(books);

        mockMvc.perform(get("/books")
                        .param("title", "Livro teste")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Livro teste"))
                .andExpect(jsonPath("$[0].author").value("Autor teste"))
                .andExpect(jsonPath("$[0].isbn").value("9788535902778"));

        verify(bookService).getAllBooks("Livro teste", null, null, null, null, null, null, null);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void getAllBooks_deveRetornarOk_quandoTodosFiltrosForemInformados() throws Exception {
        BookResponseDTO bookResponseDTO = criarBookResponseDTO();

        List<BookResponseDTO> books = List.of(bookResponseDTO);

        when(bookService.getAllBooks(
                "Livro teste",
                "Autor teste",
                "9788535902778",
                2024L,
                "Português",
                10,
                BookStatus.AVAILABLE,
                RecordStatus.ACTIVE
        )).thenReturn(books);

        mockMvc.perform(get("/books")
                        .param("title", "Livro teste")
                        .param("author", "Autor teste")
                        .param("isbn", "9788535902778")
                        .param("publicationYear", "2024")
                        .param("language", "Português")
                        .param("totalQuantity", "10")
                        .param("bookStatus", "AVAILABLE")
                        .param("recordStatus", "ACTIVE")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Livro teste"))
                .andExpect(jsonPath("$[0].bookStatus").value("AVAILABLE"))
                .andExpect(jsonPath("$[0].recordStatus").value("ACTIVE"));

        verify(bookService).getAllBooks(
                "Livro teste",
                "Autor teste",
                "9788535902778",
                2024L,
                "Português",
                10,
                BookStatus.AVAILABLE,
                RecordStatus.ACTIVE
        );
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void getAllBooks_deveRetornarBadRequest_quandoPublicationYearNaoForNumerico() throws Exception {
        mockMvc.perform(get("/books")
                        .param("publicationYear", "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    @Test
    public void getAllBooks_deveRetornarBadRequest_quandoTotalQuantityNaoForNumerico() throws Exception {
        mockMvc.perform(get("/books")
                        .param("totalQuantity", "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    @Test
    public void getAllBooks_deveRetornarBadRequest_quandoBookStatusForInvalido() throws Exception {
        mockMvc.perform(get("/books")
                        .param("bookStatus", "STATUS_INVALIDO")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    @Test
    public void getAllBooks_deveRetornarBadRequest_quandoRecordStatusForInvalido() throws Exception {
        mockMvc.perform(get("/books")
                        .param("recordStatus", "STATUS_INVALIDO")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    // Testes para o metodo 'getBookById'
    @Test
    public void getBookById_deveRetornarOk_quandoIdForValido() throws Exception {
        Long idBook = 1L;

        BookResponseDTO bookResponseDTO = criarBookResponseDTO();

        when(bookService.getBookById(idBook))
                .thenReturn(bookResponseDTO);

        mockMvc.perform(get("/books/{id}", idBook)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Livro teste"))
                .andExpect(jsonPath("$.author").value("Autor teste"))
                .andExpect(jsonPath("$.isbn").value("9788535902778"))
                .andExpect(jsonPath("$.publicationYear").value(2024))
                .andExpect(jsonPath("$.language").value("Português"))
                .andExpect(jsonPath("$.totalQuantity").value(10))
                .andExpect(jsonPath("$.availableQuantity").value(8))
                .andExpect(jsonPath("$.registrationDate").value("2024-01-10"))
                .andExpect(jsonPath("$.bookStatus").value("AVAILABLE"))
                .andExpect(jsonPath("$.recordStatus").value("ACTIVE"));

        verify(bookService).getBookById(idBook);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void getBookById_deveRetornarBadRequest_quandoIdNaoForNumerico() throws Exception {
        mockMvc.perform(get("/books/{id}", "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    @Test
    public void getBookById_deveRetornarNotFound_quandoIdNaoForInformadoNaRota() throws Exception {
        mockMvc.perform(get("/books/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verifyNoInteractions(bookService);
    }

    // Testes para o metodo 'createBook'
    @Test
    public void createBook_deveRetornarCreated_quandoBodyForValido() throws Exception {
        BookResponseDTO bookResponseDTO = criarBookResponseDTO();

        String requestJson = """
                {
                    "title": "Livro teste",
                    "author": "Autor teste",
                    "isbn": "9788535902778",
                    "publicationYear": 2024,
                    "language": "Português",
                    "totalQuantity": 10
                }
                """;

        when(bookService.createBook(any(BookCreateDTO.class)))
                .thenReturn(bookResponseDTO);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Livro teste"))
                .andExpect(jsonPath("$.author").value("Autor teste"))
                .andExpect(jsonPath("$.isbn").value("9788535902778"))
                .andExpect(jsonPath("$.publicationYear").value(2024))
                .andExpect(jsonPath("$.language").value("Português"))
                .andExpect(jsonPath("$.totalQuantity").value(10))
                .andExpect(jsonPath("$.availableQuantity").value(8))
                .andExpect(jsonPath("$.bookStatus").value("AVAILABLE"))
                .andExpect(jsonPath("$.recordStatus").value("ACTIVE"));

        verify(bookService, times(1)).createBook(any(BookCreateDTO.class));
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void createBook_deveRetornarBadRequest_quandoRequisicaoEstiverMalFormatada() throws Exception {
        String requestJson = """
                {
                    "title": "Livro teste",
                    "author": "Autor teste",
                    "isbn": "9788535902778"
                """;

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    @Test
    public void createBook_deveRetornarBadRequest_quandoBodyEstiverVazio() throws Exception {
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    @Test
    public void createBook_deveRetornarUnsupportedMediaType_quandoContentTypeForInvalido() throws Exception {
        String requestJson = """
                {
                    "title": "Livro teste",
                    "author": "Autor teste",
                    "isbn": "9788535902778",
                    "publicationYear": 2024,
                    "language": "Português",
                    "totalQuantity": 10
                }
                """;

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_XML)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isUnsupportedMediaType());

        verifyNoInteractions(bookService);
    }

    // Testes para o metodo 'partiallyUpdateBook'
    @Test
    public void partiallyUpdateBook_deveRetornarOk_quandoBodyForValido() throws Exception {
        Long idBook = 1L;

        BookResponseDTO bookResponseDTO = criarBookResponseDTO();

        String requestJson = """
                {
                    "title": "Livro teste atualizado"
                }
                """;

        bookResponseDTO.setTitle("Livro teste atualizado");

        when(bookService.partiallyUpdateBook(any(BookPatchDTO.class), eq(idBook)))
                .thenReturn(bookResponseDTO);

        mockMvc.perform(patch("/books/{id}", idBook)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Livro teste atualizado"))
                .andExpect(jsonPath("$.author").value("Autor teste"))
                .andExpect(jsonPath("$.isbn").value("9788535902778"));

        verify(bookService).partiallyUpdateBook(any(BookPatchDTO.class), eq(idBook));
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void partiallyUpdateBook_deveRetornarBadRequest_quandoIdNaoForNumerico() throws Exception {
        String requestJson = """
                {
                    "title": "Livro teste atualizado"
                }
                """;

        mockMvc.perform(patch("/books/{id}", "abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    @Test
    public void partiallyUpdateBook_deveRetornarBadRequest_quandoBodyEstiverVazio() throws Exception {
        mockMvc.perform(patch("/books/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    @Test
    public void partiallyUpdateBook_deveRetornarUnsupportedMediaType_quandoContentTypeForInvalido() throws Exception {
        String requestJson = """
                {
                    "title": "Livro teste atualizado"
                }
                """;

        mockMvc.perform(patch("/books/{id}", 1)
                        .contentType(MediaType.APPLICATION_XML)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isUnsupportedMediaType());

        verifyNoInteractions(bookService);
    }

    // Testes para o metodo 'updateBook'
    @Test
    public void updateBook_deveRetornarOk_quandoBodyForValido() throws Exception {
        Long idBook = 1L;

        BookResponseDTO bookResponseDTO = criarBookResponseDTO();

        String requestJson = """
                {
                    "title": "Livro teste",
                    "author": "Autor teste",
                    "isbn": "9788535902778",
                    "publicationYear": 2024,
                    "language": "Português",
                    "totalQuantity": 10
                }
                """;

        when(bookService.updateBook(any(BookUpdateDTO.class), eq(idBook)))
                .thenReturn(bookResponseDTO);

        mockMvc.perform(put("/books/{id}", idBook)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Livro teste"))
                .andExpect(jsonPath("$.author").value("Autor teste"))
                .andExpect(jsonPath("$.isbn").value("9788535902778"))
                .andExpect(jsonPath("$.publicationYear").value(2024))
                .andExpect(jsonPath("$.language").value("Português"))
                .andExpect(jsonPath("$.totalQuantity").value(10))
                .andExpect(jsonPath("$.availableQuantity").value(8));

        verify(bookService).updateBook(any(BookUpdateDTO.class), eq(idBook));
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void updateBook_deveRetornarBadRequest_quandoIdNaoForNumerico() throws Exception {
        String requestJson = """
                {
                    "title": "Livro teste",
                    "author": "Autor teste",
                    "isbn": "9788535902778",
                    "publicationYear": 2024,
                    "language": "Português",
                    "totalQuantity": 10
                }
                """;

        mockMvc.perform(put("/books/{id}", "abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    @Test
    public void updateBook_deveRetornarBadRequest_quandoBodyEstiverVazio() throws Exception {
        mockMvc.perform(put("/books/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    @Test
    public void updateBook_deveRetornarBadRequest_quandoRequisicaoEstiverMalFormatada() throws Exception {
        String requestJson = """
                {
                    "title": "Livro teste",
                    "author": "Autor teste",
                    "isbn": "9788535902778"
                """;

        mockMvc.perform(put("/books/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    // Testes para o metodo 'deactivateBook'
    @Test
    public void deactivateBook_deveRetornarNoContent_quandoIdForValido() throws Exception {
        Long idBook = 1L;

        doNothing().when(bookService).deactivateBook(idBook);

        mockMvc.perform(patch("/books/{id}/deactivate", idBook))
                .andExpect(status().isNoContent());

        verify(bookService).deactivateBook(idBook);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void deactivateBook_deveRetornarBadRequest_quandoIdNaoForNumerico() throws Exception {
        mockMvc.perform(patch("/books/{id}/deactivate", "abc"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    // Testes para o metodo 'activateBook'
    @Test
    public void activateBook_deveRetornarNoContent_quandoIdForValido() throws Exception {
        Long idBook = 1L;

        doNothing().when(bookService).activateBook(idBook);

        mockMvc.perform(patch("/books/{id}/activate", idBook))
                .andExpect(status().isNoContent());

        verify(bookService).activateBook(idBook);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void activateBook_deveRetornarBadRequest_quandoIdNaoForNumerico() throws Exception {
        mockMvc.perform(patch("/books/{id}/activate", "abc"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    // Testes para o metodo 'addStock'
    @Test
    public void addStock_deveRetornarOk_quandoBodyForValido() throws Exception {
        Long idBook = 1L;

        BookResponseDTO bookResponseDTO = criarBookResponseDTO();
        bookResponseDTO.setTotalQuantity(12);
        bookResponseDTO.setAvailableQuantity(10);

        String requestJson = """
                {
                    "quantity": 2
                }
                """;

        when(bookService.addStock(any(StockDTO.class), eq(idBook)))
                .thenReturn(bookResponseDTO);

        mockMvc.perform(patch("/books/{id}/add-stock", idBook)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Livro teste"))
                .andExpect(jsonPath("$.totalQuantity").value(12))
                .andExpect(jsonPath("$.availableQuantity").value(10));

        verify(bookService).addStock(any(StockDTO.class), eq(idBook));
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void addStock_deveRetornarBadRequest_quandoIdNaoForNumerico() throws Exception {
        String requestJson = """
                {
                    "quantity": 2
                }
                """;

        mockMvc.perform(patch("/books/{id}/add-stock", "abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    @Test
    public void addStock_deveRetornarBadRequest_quandoBodyEstiverVazio() throws Exception {
        mockMvc.perform(patch("/books/{id}/add-stock", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    @Test
    public void addStock_deveRetornarBadRequest_quandoRequisicaoEstiverMalFormatada() throws Exception {
        String requestJson = """
                {
                    "quantity": 2
                """;

        mockMvc.perform(patch("/books/{id}/add-stock", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    @Test
    public void addStock_deveRetornarUnsupportedMediaType_quandoContentTypeForInvalido() throws Exception {
        String requestJson = """
                {
                    "quantity": 2
                }
                """;

        mockMvc.perform(patch("/books/{id}/add-stock", 1)
                        .contentType(MediaType.APPLICATION_XML)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isUnsupportedMediaType());

        verifyNoInteractions(bookService);
    }

    // Testes para o metodo 'removeStock'
    @Test
    public void removeStock_deveRetornarOk_quandoBodyForValido() throws Exception {
        Long idBook = 1L;

        BookResponseDTO bookResponseDTO = criarBookResponseDTO();
        bookResponseDTO.setTotalQuantity(8);
        bookResponseDTO.setAvailableQuantity(6);

        String requestJson = """
                {
                    "quantity": 2
                }
                """;

        when(bookService.removeStock(any(StockDTO.class), eq(idBook)))
                .thenReturn(bookResponseDTO);

        mockMvc.perform(patch("/books/{id}/remove-stock", idBook)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Livro teste"))
                .andExpect(jsonPath("$.totalQuantity").value(8))
                .andExpect(jsonPath("$.availableQuantity").value(6));

        verify(bookService).removeStock(any(StockDTO.class), eq(idBook));
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void removeStock_deveRetornarBadRequest_quandoIdNaoForNumerico() throws Exception {
        String requestJson = """
                {
                    "quantity": 2
                }
                """;

        mockMvc.perform(patch("/books/{id}/remove-stock", "abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    @Test
    public void removeStock_deveRetornarBadRequest_quandoBodyEstiverVazio() throws Exception {
        mockMvc.perform(patch("/books/{id}/remove-stock", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    @Test
    public void removeStock_deveRetornarBadRequest_quandoRequisicaoEstiverMalFormatada() throws Exception {
        String requestJson = """
                {
                    "quantity": 2
                """;

        mockMvc.perform(patch("/books/{id}/remove-stock", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    @Test
    public void removeStock_deveRetornarUnsupportedMediaType_quandoContentTypeForInvalido() throws Exception {
        String requestJson = """
                {
                    "quantity": 2
                }
                """;

        mockMvc.perform(patch("/books/{id}/remove-stock", 1)
                        .contentType(MediaType.APPLICATION_XML)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isUnsupportedMediaType());

        verifyNoInteractions(bookService);
    }
}