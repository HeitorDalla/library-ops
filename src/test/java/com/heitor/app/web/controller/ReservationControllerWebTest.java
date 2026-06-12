package com.heitor.app.web.controller;

import com.heitor.app.controller.ReservationController;
import com.heitor.app.dto.input.ReservationRequestDTO;
import com.heitor.app.dto.output.ReservationResponseDTO;
import com.heitor.app.service.ReservationService;
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
public class ReservationControllerWebTest {
    @InjectMocks
    private ReservationController reservationController;

    @Mock
    private ReservationService reservationService;

    @Test
    public void deveCriarReservaERetornarCreated() {
        ReservationRequestDTO reservationRequestDTO = new ReservationRequestDTO();
        reservationRequestDTO.setUserId(1L);
        reservationRequestDTO.setBookId(1L);

        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setId(1L);

        when(reservationService.createReservation(reservationRequestDTO))
                .thenReturn(reservationResponseDTO);

        ResponseEntity<ReservationResponseDTO> resultado = reservationController.createReservation(reservationRequestDTO);

        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals(1L, resultado.getBody().getId());

        verify(reservationService).createReservation(reservationRequestDTO);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    public void deveRetornarReservaERetornarOk() {
        Long idReservation = 1L;

        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setId(idReservation);

        when(reservationService.returnReservation(idReservation))
                .thenReturn(reservationResponseDTO);

        ResponseEntity<ReservationResponseDTO> resultado = reservationController.returnReservation(idReservation);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals(idReservation, resultado.getBody().getId());

        verify(reservationService).returnReservation(idReservation);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    public void deveCancelarReservaERetornarNoContent() {
        Long idReservation = 1L;

        doNothing().when(reservationService)
                .cancelReservation(idReservation);

        ResponseEntity<Void> resultado = reservationController.cancelReservation(idReservation);

        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
        assertNull(resultado.getBody());

        verify(reservationService).cancelReservation(idReservation);
        verifyNoMoreInteractions(reservationService);
    }
}
