package com.heitor.app.web.controller;

import com.heitor.app.controller.LoanController;
import com.heitor.app.dto.input.LoanRequestDTO;
import com.heitor.app.dto.output.LoanResponseDTO;
import com.heitor.app.enums.LoanStatus;
import com.heitor.app.enums.RecordStatus;
import com.heitor.app.service.LoanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
public class LoanControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanService loanService;

    private LoanResponseDTO criarLoanResponseDto() {
        LoanResponseDTO loanResponseDTO = new LoanResponseDTO();
        loanResponseDTO.setId(1L);

        return loanResponseDTO;
    }

    // Testes para o metodo 'getAllLoans'
    @Test
    public void getAllLoans_deveRetornarOk_quandoNaoHouverFiltros() throws Exception {
        LoanResponseDTO loanResponseDTO = criarLoanResponseDto();

        List<LoanResponseDTO> loans = List.of(loanResponseDTO);

        when(loanService.getAllLoans(null, null, null, null))
                .thenReturn(loans);

        mockMvc.perform(get("/loans")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(loanService, times(1)).getAllLoans(null, null, null, null);
        verifyNoMoreInteractions(loanService);
    }

    @Test
    public void getAllLoans_deveRetornarOk_quandoUserIdForInformado() throws Exception {
        LoanResponseDTO loanResponseDTO = criarLoanResponseDto();

        List<LoanResponseDTO> loans = List.of(loanResponseDTO);

        when(loanService.getAllLoans(1L, null, null, null))
                .thenReturn(loans);

        mockMvc.perform(get("/loans")
                        .param("userId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(loanService).getAllLoans(1L, null, null, null);
        verifyNoMoreInteractions(loanService);
    }

    @Test
    public void getAllLoans_deveRetornarOk_quandoFineForInformado() throws Exception {
        LoanResponseDTO loanResponseDTO = criarLoanResponseDto();

        List<LoanResponseDTO> loans = List.of(loanResponseDTO);

        when(loanService.getAllLoans(null, true, null, null))
                .thenReturn(loans);

        mockMvc.perform(get("/loans")
                        .param("fine", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(loanService).getAllLoans(null, true, null, null);
        verifyNoMoreInteractions(loanService);
    }

    @Test
    public void getAllLoans_deveRetornarOk_quandoLoanStatusForInformado() throws Exception {
        LoanResponseDTO loanResponseDTO = criarLoanResponseDto();

        List<LoanResponseDTO> loans = List.of(loanResponseDTO);


        when(loanService.getAllLoans(null, null, LoanStatus.OPEN, null))
                .thenReturn(loans);

        mockMvc.perform(get("/loans")
                        .param("loanStatus", "OPEN")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(loanService).getAllLoans(null, null, LoanStatus.OPEN, null);
        verifyNoMoreInteractions(loanService);
    }

    @Test
    public void getAllLoans_deveRetornarOk_quandoRecordStatusForInformado() throws Exception {
        LoanResponseDTO loanResponseDTO = criarLoanResponseDto();

        List<LoanResponseDTO> loans = List.of(loanResponseDTO);

        when(loanService.getAllLoans(null, null, null, RecordStatus.ACTIVE))
                .thenReturn(loans);

        mockMvc.perform(get("/loans")
                        .param("recordStatus", "ACTIVE")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(loanService).getAllLoans(null, null, null, RecordStatus.ACTIVE);
        verifyNoMoreInteractions(loanService);
    }

    @Test
    public void getAllLoans_deveRetornarBadRequest_quandoUserIdNaoForNumerico() throws Exception {
        mockMvc.perform(get("/loans")
                        .param("userId", "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(loanService);
    }

    @Test
    public void getAllLoans_deveRetornarBadRequest_quandoFineNaoForBooleano() throws Exception {
        mockMvc.perform(get("/loans")
                        .param("fine", "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(loanService);
    }

    @Test
    public void getAllLoans_deveRetornarBadRequest_quandoLoanStatusForInvalido() throws Exception {
        mockMvc.perform(get("/loans")
                        .param("loanStatus", "INVALIDO")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(loanService);
    }

    @Test
    public void getAllLoans_deveRetornarBadRequest_quandoRecordStatusForInvalido() throws Exception {
        mockMvc.perform(get("/loans")
                        .param("recordStatus", "INVALIDO")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(loanService);
    }

    // Testes para o metodo 'getLoanById'
    @Test
    public void getLoanById_deveRetornarOk_quandoIdForValido() throws Exception {
        Long idLoan = 1L;

        LoanResponseDTO loanResponseDTO = criarLoanResponseDto();

        when(loanService.getLoanById(idLoan))
                .thenReturn(loanResponseDTO);

        mockMvc.perform(get("/loans/{id}", idLoan)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(loanService).getLoanById(idLoan);
        verifyNoMoreInteractions(loanService);
    }

    @Test
    public void getLoanById_deveRetornarBadRequest_quandoIdNaoForNumerico() throws Exception {
        mockMvc.perform(get("/loans/{id}", "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(loanService);
    }

    @Test
    public void getLoanById_deveRetornarNotFound_quandoIdNaoForInformadoNaRota() throws Exception {
        mockMvc.perform(get("/loans/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verifyNoInteractions(loanService);
    }

    // Testes para o metodo 'createLoan'
    @Test
    public void createLoan_deveRetornarCreated_quandoBodyForValido() throws Exception {
        LoanResponseDTO loanResponseDTO = criarLoanResponseDto();

        String requestJson = """
                {
                    "userId": 1,
                    "bookId": 1
                }
                """;

        when(loanService.createLoan(any(LoanRequestDTO.class)))
                .thenReturn(loanResponseDTO);

        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(loanService, times(1)).createLoan(any(LoanRequestDTO.class));
        verifyNoMoreInteractions(loanService);
    }

    @Test
    public void createLoan_deveRetornarBadRequest_quandoRequisicaoEstiverMalFormatada() throws Exception {
        String requestJson = """
                {
                    "userId": 1,
                    "bookId": 1
                """;

        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(loanService);
    }

    @Test
    public void createLoan_deveRetornarBadRequest_quandoBodyEstiverVazio() throws Exception {
        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(loanService);
    }

    @Test
    public void createLoan_deveRetornarBadRequest_quandoUserIdNaoForNumerico() throws Exception {
        String requestJson = """
                {
                    "userId": "abc",
                    "bookId": 1
                }
                """;

        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(loanService);
    }

    @Test
    public void createLoan_deveRetornarBadRequest_quandoBookIdNaoForNumerico() throws Exception {
        String requestJson = """
                {
                    "userId": 1,
                    "bookId": "abc"
                }
                """;

        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(loanService);
    }

    @Test
    public void createLoan_deveRetornarUnsupportedMediaType_quandoContentTypeForInvalido() throws Exception {
        String requestJson = """
                {
                    "userId": 1,
                    "bookId": 1
                }
                """;

        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_XML)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isUnsupportedMediaType());

        verifyNoInteractions(loanService);
    }

    // Testes para o metodo 'returnLoan'
    @Test
    public void returnLoan_deveRetornarOk_quandoIdForValido() throws Exception {
        Long idLoan = 1L;

        LoanResponseDTO loanResponseDTO = criarLoanResponseDto();

        when(loanService.returnLoan(idLoan))
                .thenReturn(loanResponseDTO);

        mockMvc.perform(put("/loans/{id}/return", idLoan)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(loanService).returnLoan(idLoan);
        verifyNoMoreInteractions(loanService);
    }

    @Test
    public void returnLoan_deveRetornarBadRequest_quandoIdNaoForNumerico() throws Exception {
        mockMvc.perform(put("/loans/{id}/return", "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(loanService);
    }

    @Test
    public void returnLoan_deveRetornarNotFound_quandoIdNaoForInformadoNaRota() throws Exception {
        mockMvc.perform(put("/loans/return")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verifyNoInteractions(loanService);
    }

    // Testes para o metodo 'cancelLoan'
    @Test
    public void cancelLoan_deveRetornarNoContent_quandoIdForValido() throws Exception {
        Long idLoan = 1L;

        doNothing().when(loanService).cancelLoan(idLoan);

        mockMvc.perform(delete("/loans/{id}", idLoan))
                .andExpect(status().isNoContent());

        verify(loanService).cancelLoan(idLoan);
        verifyNoMoreInteractions(loanService);
    }

    @Test
    public void cancelLoan_deveRetornarBadRequest_quandoIdNaoForNumerico() throws Exception {
        mockMvc.perform(delete("/loans/{id}", "abc"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(loanService);
    }

    @Test
    public void cancelLoan_deveRetornarNotFound_quandoIdNaoForInformadoNaRota() throws Exception {
        mockMvc.perform(delete("/loans/"))
                .andExpect(status().isNotFound());

        verifyNoInteractions(loanService);
    }
}