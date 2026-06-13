package com.heitor.app.web.controller;

import com.heitor.app.controller.FineController;
import com.heitor.app.dto.output.FineResponseDTO;
import com.heitor.app.enums.FineStatus;
import com.heitor.app.enums.RecordStatus;
import com.heitor.app.service.FineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FineController.class)
public class FineControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FineService fineService;

    private FineResponseDTO criarFineResponseDTO() {
        FineResponseDTO fineResponseDTO = new FineResponseDTO();

        fineResponseDTO.setId(1L);
        fineResponseDTO.setAmount(new BigDecimal("25.50"));
        fineResponseDTO.setFineStatus(FineStatus.OPEN);
        fineResponseDTO.setCreatedDate(LocalDate.of(2024, 1, 10));
        fineResponseDTO.setPaymentDate(null);
        fineResponseDTO.setLoanId(10L);
        fineResponseDTO.setRecordStatus(RecordStatus.ACTIVE);

        return fineResponseDTO;
    }

    // Testes para o metodo 'getAllFines'
    @Test
    public void getAllFines_deveRetornarOk_quandoNaoHouverFiltros() throws Exception {
        FineResponseDTO fineResponseDTO = criarFineResponseDTO();

        List<FineResponseDTO> fines = List.of(fineResponseDTO);

        when(fineService.getAllFines(null, null, null))
                .thenReturn(fines);

        mockMvc.perform(get("/fines")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].amount").value(25.50))
                .andExpect(jsonPath("$[0].fineStatus").value("OPEN"))
                .andExpect(jsonPath("$[0].createdDate").value("2024-01-10"))
                .andExpect(jsonPath("$[0].paymentDate").doesNotExist())
                .andExpect(jsonPath("$[0].loanId").value(10))
                .andExpect(jsonPath("$[0].recordStatus").value("ACTIVE"));

        verify(fineService, times(1)).getAllFines(null, null, null);
        verifyNoMoreInteractions(fineService);
    }

    @Test
    public void getAllFines_deveRetornarOk_quandoAmountForInformado() throws Exception {
        FineResponseDTO fineResponseDTO = criarFineResponseDTO();

        List<FineResponseDTO> fines = List.of(fineResponseDTO);

        when(fineService.getAllFines(new BigDecimal("25.50"), null, null))
                .thenReturn(fines);

        mockMvc.perform(get("/fines")
                        .param("amount", "25.50")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].amount").value(25.50))
                .andExpect(jsonPath("$[0].fineStatus").value("OPEN"))
                .andExpect(jsonPath("$[0].loanId").value(10));

        verify(fineService).getAllFines(new BigDecimal("25.50"), null, null);
        verifyNoMoreInteractions(fineService);
    }

    @Test
    public void getAllFines_deveRetornarOk_quandoFineStatusForInformado() throws Exception {
        FineResponseDTO fineResponseDTO = criarFineResponseDTO();

        List<FineResponseDTO> fines = List.of(fineResponseDTO);

        when(fineService.getAllFines(null, FineStatus.OPEN, null))
                .thenReturn(fines);

        mockMvc.perform(get("/fines")
                        .param("fineStatus", "OPEN")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fineStatus").value("OPEN"));

        verify(fineService).getAllFines(null, FineStatus.OPEN, null);
        verifyNoMoreInteractions(fineService);
    }

    @Test
    public void getAllFines_deveRetornarOk_quandoRecordStatusForInformado() throws Exception {
        FineResponseDTO fineResponseDTO = criarFineResponseDTO();

        List<FineResponseDTO> fines = List.of(fineResponseDTO);

        when(fineService.getAllFines(null, null, RecordStatus.ACTIVE))
                .thenReturn(fines);

        mockMvc.perform(get("/fines")
                        .param("recordStatus", "ACTIVE")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].recordStatus").value("ACTIVE"));

        verify(fineService).getAllFines(null, null, RecordStatus.ACTIVE);
        verifyNoMoreInteractions(fineService);
    }

    @Test
    public void getAllFines_deveRetornarOk_quandoTodosFiltrosForemInformados() throws Exception {
        FineResponseDTO fineResponseDTO = criarFineResponseDTO();

        List<FineResponseDTO> fines = List.of(fineResponseDTO);

        when(fineService.getAllFines(
                new BigDecimal("25.50"),
                FineStatus.OPEN,
                RecordStatus.ACTIVE
        )).thenReturn(fines);

        mockMvc.perform(get("/fines")
                        .param("amount", "25.50")
                        .param("fineStatus", "OPEN")
                        .param("recordStatus", "ACTIVE")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].amount").value(25.50))
                .andExpect(jsonPath("$[0].fineStatus").value("OPEN"))
                .andExpect(jsonPath("$[0].recordStatus").value("ACTIVE"));

        verify(fineService).getAllFines(
                new BigDecimal("25.50"),
                FineStatus.OPEN,
                RecordStatus.ACTIVE
        );
        verifyNoMoreInteractions(fineService);
    }

    @Test
    public void getAllFines_deveRetornarBadRequest_quandoAmountNaoForNumerico() throws Exception {
        mockMvc.perform(get("/fines")
                        .param("amount", "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(fineService);
    }

    @Test
    public void getAllFines_deveRetornarBadRequest_quandoFineStatusForInvalido() throws Exception {
        mockMvc.perform(get("/fines")
                        .param("fineStatus", "STATUS_INVALIDO")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(fineService);
    }

    @Test
    public void getAllFines_deveRetornarBadRequest_quandoRecordStatusForInvalido() throws Exception {
        mockMvc.perform(get("/fines")
                        .param("recordStatus", "STATUS_INVALIDO")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(fineService);
    }

    // Testes para o metodo 'getFineById'
    @Test
    public void getFineById_deveRetornarOk_quandoIdForValido() throws Exception {
        Long idFine = 1L;

        FineResponseDTO fineResponseDTO = criarFineResponseDTO();

        when(fineService.getFineById(idFine))
                .thenReturn(fineResponseDTO);

        mockMvc.perform(get("/fines/{id}", idFine)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(25.50))
                .andExpect(jsonPath("$.fineStatus").value("OPEN"))
                .andExpect(jsonPath("$.createdDate").value("2024-01-10"))
                .andExpect(jsonPath("$.paymentDate").doesNotExist())
                .andExpect(jsonPath("$.loanId").value(10))
                .andExpect(jsonPath("$.recordStatus").value("ACTIVE"));

        verify(fineService).getFineById(idFine);
        verifyNoMoreInteractions(fineService);
    }

    @Test
    public void getFineById_deveRetornarBadRequest_quandoIdNaoForNumerico() throws Exception {
        mockMvc.perform(get("/fines/{id}", "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(fineService);
    }

    @Test
    public void getFineById_deveRetornarNotFound_quandoIdNaoForInformadoNaRota() throws Exception {
        mockMvc.perform(get("/fines/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verifyNoInteractions(fineService);
    }

    // Testes para o metodo 'payFine'
    @Test
    public void payFine_deveRetornarOk_quandoIdForValido() throws Exception {
        Long idFine = 1L;

        FineResponseDTO fineResponseDTO = criarFineResponseDTO();
        fineResponseDTO.setFineStatus(FineStatus.PAID);
        fineResponseDTO.setPaymentDate(LocalDate.of(2024, 1, 15));

        when(fineService.payFine(idFine))
                .thenReturn(fineResponseDTO);

        mockMvc.perform(patch("/fines/{id}/payment", idFine)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(25.50))
                .andExpect(jsonPath("$.fineStatus").value("PAID"))
                .andExpect(jsonPath("$.createdDate").value("2024-01-10"))
                .andExpect(jsonPath("$.paymentDate").value("2024-01-15"))
                .andExpect(jsonPath("$.loanId").value(10))
                .andExpect(jsonPath("$.recordStatus").value("ACTIVE"));

        verify(fineService).payFine(idFine);
        verifyNoMoreInteractions(fineService);
    }

    @Test
    public void payFine_deveRetornarBadRequest_quandoIdNaoForNumerico() throws Exception {
        mockMvc.perform(patch("/fines/{id}/payment", "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(fineService);
    }

    @Test
    public void payFine_deveRetornarMethodAllowed_quandoIdNaoForInformadoNaRota() throws Exception {
        mockMvc.perform(patch("/fines/payment")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        verifyNoInteractions(fineService);
    }

    // Testes para o metodo 'cancelFine'
    @Test
    public void cancelFine_deveRetornarNoContent_quandoIdForValido() throws Exception {
        Long idFine = 1L;

        doNothing().when(fineService).cancelFine(idFine);

        mockMvc.perform(patch("/fines/{id}/cancel", idFine)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(fineService).cancelFine(idFine);
        verifyNoMoreInteractions(fineService);
    }

    @Test
    public void cancelFine_deveRetornarBadRequest_quandoIdNaoForNumerico() throws Exception {
        mockMvc.perform(patch("/fines/{id}/cancel", "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(fineService);
    }

    @Test
    public void cancelFine_deveRetornarMethodNotAllowed_quandoIdNaoForInformadoNaRota() throws Exception {
        mockMvc.perform(patch("/fines/cancel")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        verifyNoInteractions(fineService);
    }
}