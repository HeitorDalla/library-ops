package com.heitor.app.controller;

import com.heitor.app.dto.output.FineResponseDTO;
import com.heitor.app.service.FineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FineControllerTest {

    @InjectMocks
    private FineController fineController;

    @Mock
    private FineService fineService;

    @Test
    public void devePagarMultaERetornarOk() {

        Long idFine = 1L;

        FineResponseDTO fineResponseDTO = new FineResponseDTO();
        fineResponseDTO.setAmount(BigDecimal.valueOf(40));

        when(fineService.payFine(idFine))
                .thenReturn(fineResponseDTO);

        ResponseEntity<FineResponseDTO> resultado = fineController.payFine(idFine);

        assertNotNull(resultado.getBody());
        assertEquals(HttpStatus.OK, resultado.getStatusCode());

        verify(fineService).payFine(idFine);
        assertEquals(BigDecimal.valueOf(40), resultado.getBody().getAmount());
        verifyNoMoreInteractions(fineService);
    }

    @Test
    public void deveCancelarMultaERetornarNoContent() {

        Long idFine = 1L;

        doNothing().when(fineService).cancelFine(idFine);

        ResponseEntity<Void> resultado = fineController.cancelFine(idFine);

        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
        assertNull(resultado.getBody());

        verify(fineService).cancelFine(idFine);
        verifyNoMoreInteractions(fineService);
    }
}