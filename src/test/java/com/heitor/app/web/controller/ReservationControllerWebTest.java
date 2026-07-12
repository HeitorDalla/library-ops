package com.heitor.app.web.controller;

import com.heitor.app.controller.ReservationController;
import com.heitor.app.dto.input.ReservationRequestDTO;
import com.heitor.app.dto.output.ReservationResponseDTO;
import com.heitor.app.enums.RecordStatus;
import com.heitor.app.enums.ReservationStatus;
import com.heitor.app.service.ReservationService;
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

@WebMvcTest(ReservationController.class)
public class ReservationControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    private ReservationResponseDTO criarReservationResponseDto() {
        return ReservationResponseDTO.builder()
                .id(1L)
                .build();
    }

    // Testes para o metodo 'getAllReservations'
    @Test
    public void getAllReservations_deveRetornarOk_quandoNaoHouverFiltros() throws Exception {
        ReservationResponseDTO reservationResponseDTO = criarReservationResponseDto();

        List<ReservationResponseDTO> reservations = List.of(reservationResponseDTO);

        when(reservationService.getAllReservations(null, null, null, null))
                .thenReturn(reservations);

        mockMvc.perform(get("/reservations")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(reservationService, times(1)).getAllReservations(null, null, null, null);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    public void getAllReservations_deveRetornarOk_quandoUserIdForInformado() throws Exception {
        ReservationResponseDTO reservationResponseDTO = criarReservationResponseDto();

        List<ReservationResponseDTO> reservations = List.of(reservationResponseDTO);

        when(reservationService.getAllReservations(1L, null, null, null))
                .thenReturn(reservations);

        mockMvc.perform(get("/reservations")
                        .param("userId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(reservationService).getAllReservations(1L, null, null, null);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    public void getAllReservations_deveRetornarOk_quandoBookIdForInformado() throws Exception {
        ReservationResponseDTO reservationResponseDTO = criarReservationResponseDto();

        List<ReservationResponseDTO> reservations = List.of(reservationResponseDTO);

        when(reservationService.getAllReservations(null, 1L, null, null))
                .thenReturn(reservations);

        mockMvc.perform(get("/reservations")
                        .param("bookId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(reservationService).getAllReservations(null, 1L, null, null);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    public void getAllReservations_deveRetornarOk_quandoReservationStatusForInformado() throws Exception {
        ReservationResponseDTO reservationResponseDTO = criarReservationResponseDto();

        List<ReservationResponseDTO> reservations = List.of(reservationResponseDTO);

        when(reservationService.getAllReservations(null, null, ReservationStatus.PENDING, null))
                .thenReturn(reservations);

        mockMvc.perform(get("/reservations")
                        .param("reservationStatus", "PENDING")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(reservationService).getAllReservations(null, null, ReservationStatus.PENDING, null);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    public void getAllReservations_deveRetornarOk_quandoRecordStatusForInformado() throws Exception {
        ReservationResponseDTO reservationResponseDTO = criarReservationResponseDto();

        List<ReservationResponseDTO> reservations = List.of(reservationResponseDTO);

        when(reservationService.getAllReservations(null, null, null, RecordStatus.ACTIVE))
                .thenReturn(reservations);

        mockMvc.perform(get("/reservations")
                        .param("recordStatus", "ACTIVE")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(reservationService).getAllReservations(null, null, null, RecordStatus.ACTIVE);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    public void getAllReservations_deveRetornarBadRequest_quandoUserIdNaoForNumerico() throws Exception {
        mockMvc.perform(get("/reservations")
                        .param("userId", "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(reservationService);
    }

    @Test
    public void getAllReservations_deveRetornarBadRequest_quandoBookIdNaoForNumerico() throws Exception {
        mockMvc.perform(get("/reservations")
                        .param("bookId", "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(reservationService);
    }

    @Test
    public void getAllReservations_deveRetornarBadRequest_quandoReservationStatusForInvalido() throws Exception {
        mockMvc.perform(get("/reservations")
                        .param("reservationStatus", "INVALIDO")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(reservationService);
    }

    @Test
    public void getAllReservations_deveRetornarBadRequest_quandoRecordStatusForInvalido() throws Exception {
        mockMvc.perform(get("/reservations")
                        .param("recordStatus", "INVALIDO")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(reservationService);
    }

    // Testes para o metodo 'getReservationById'
    @Test
    public void getReservationById_deveRetornarOk_quandoIdForValido() throws Exception {
        Long idReservation = 1L;

        ReservationResponseDTO reservationResponseDTO = criarReservationResponseDto();

        when(reservationService.getReservationById(idReservation))
                .thenReturn(reservationResponseDTO);

        mockMvc.perform(get("/reservations/{id}", idReservation)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(reservationService).getReservationById(idReservation);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    public void getReservationById_deveRetornarBadRequest_quandoIdNaoForNumerico() throws Exception {
        mockMvc.perform(get("/reservations/{id}", "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(reservationService);
    }

    @Test
    public void getReservationById_deveRetornarNotFound_quandoIdNaoForInformadoNaRota() throws Exception {
        mockMvc.perform(get("/reservations/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verifyNoInteractions(reservationService);
    }

    // Testes para o metodo 'createReservation'
    @Test
    public void createReservation_deveRetornarCreated_quandoBodyForValido() throws Exception {
        ReservationResponseDTO reservationResponseDTO = criarReservationResponseDto();

        String requestJson = """
                {
                    "userId": 1,
                    "bookId": 1
                }
                """;

        when(reservationService.createReservation(any(ReservationRequestDTO.class)))
                .thenReturn(reservationResponseDTO);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(reservationService, times(1)).createReservation(any(ReservationRequestDTO.class));
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    public void createReservation_deveRetornarBadRequest_quandoRequisicaoEstiverMalFormatada() throws Exception {
        String requestJson = """
                {
                    "userId": 1,
                    "bookId": 1
                """;

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(reservationService);
    }

    @Test
    public void createReservation_deveRetornarBadRequest_quandoBodyEstiverVazio() throws Exception {
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(reservationService);
    }

    @Test
    public void createReservation_deveRetornarBadRequest_quandoUserIdNaoForNumerico() throws Exception {
        String requestJson = """
                {
                    "userId": "abc",
                    "bookId": 1
                }
                """;

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(reservationService);
    }

    @Test
    public void createReservation_deveRetornarBadRequest_quandoBookIdNaoForNumerico() throws Exception {
        String requestJson = """
                {
                    "userId": 1,
                    "bookId": "abc"
                }
                """;

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(reservationService);
    }

    @Test
    public void createReservation_deveRetornarUnsupportedMediaType_quandoContentTypeForInvalido() throws Exception {
        String requestJson = """
                {
                    "userId": 1,
                    "bookId": 1
                }
                """;

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_XML)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isUnsupportedMediaType());

        verifyNoInteractions(reservationService);
    }

    // Testes para o metodo 'returnReservation'
    @Test
    public void returnReservation_deveRetornarOk_quandoIdForValido() throws Exception {
        Long idReservation = 1L;

        ReservationResponseDTO reservationResponseDTO = criarReservationResponseDto();

        when(reservationService.returnReservation(idReservation))
                .thenReturn(reservationResponseDTO);

        mockMvc.perform(patch("/reservations/{id}/return", idReservation)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(reservationService).returnReservation(idReservation);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    public void returnReservation_deveRetornarBadRequest_quandoIdNaoForNumerico() throws Exception {
        mockMvc.perform(patch("/reservations/{id}/return", "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(reservationService);
    }

    @Test
    public void returnReservation_deveRetornarMethodNotAllowed_quandoIdNaoForInformadoNaRota() throws Exception {
        mockMvc.perform(patch("/reservations/return")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        verifyNoInteractions(reservationService);
    }

    // Testes para o metodo 'cancelReservation'
    @Test
    public void cancelReservation_deveRetornarNoContent_quandoIdForValido() throws Exception {
        Long idReservation = 1L;

        doNothing().when(reservationService).cancelReservation(idReservation);

        mockMvc.perform(patch("/reservations/{id}/cancel", idReservation))
                .andExpect(status().isNoContent());

        verify(reservationService).cancelReservation(idReservation);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    public void cancelReservation_deveRetornarBadRequest_quandoIdNaoForNumerico() throws Exception {
        mockMvc.perform(patch("/reservations/{id}/cancel", "abc"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(reservationService);
    }

    @Test
    public void cancelReservation_deveRetornarMethodNotAllowed_quandoIdNaoForInformadoNaRota() throws Exception {
        mockMvc.perform(patch("/reservations/cancel"))
                .andExpect(status().isMethodNotAllowed());

        verifyNoInteractions(reservationService);
    }
}