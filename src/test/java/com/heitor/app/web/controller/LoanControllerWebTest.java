package com.heitor.app.web.controller;

import com.heitor.app.controller.LoanController;
import com.heitor.app.dto.input.LoanRequestDTO;
import com.heitor.app.dto.output.LoanResponseDTO;
import com.heitor.app.service.LoanService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class LoanControllerWebTest {
    @InjectMocks
    private LoanController loanController;

    @Mock
    private LoanService loanService;

    @Test
    public void deveCriarEmprestimoERetornarCreated() {
        List<Long> books = new ArrayList<>();
        books.add(1L);
        books.add(2L);

        LoanRequestDTO loanRequestDTO = new LoanRequestDTO();
        loanRequestDTO.setUserId(1L);
        loanRequestDTO.setBookIds(books);

        LoanResponseDTO loanResponseDTO = new LoanResponseDTO();
        loanResponseDTO.setId(1L);

        when(loanService.createLoan(loanRequestDTO))
                .thenReturn(loanResponseDTO);

        ResponseEntity<LoanResponseDTO> resultado = loanController.createLoan(loanRequestDTO);

        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals(1L, resultado.getBody().getId());

        verify(loanService).createLoan(loanRequestDTO);
        verifyNoMoreInteractions(loanService);
    }

    @Test
    public void deveRetornarEmprestimoERetornarOk() {
        Long idLoan = 1L;

        LoanResponseDTO loanResponseDTO = new LoanResponseDTO();
        loanResponseDTO.setId(idLoan);

        when(loanService.returnLoan(idLoan))
                .thenReturn(loanResponseDTO);

        ResponseEntity<LoanResponseDTO> resultado = loanController.returnLoan(idLoan);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals(idLoan, resultado.getBody().getId());

        verify(loanService).returnLoan(idLoan);
        verifyNoMoreInteractions(loanService);
    }

    @Test
    public void deveCancelarEmprestimoERetornarNoContent() {
        Long idLoan = 1L;

        doNothing().when(loanService).cancelLoan(idLoan);

        ResponseEntity<Void> resultado = loanController.cancelLoan(idLoan);

        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
        assertNull(resultado.getBody());

        verify(loanService).cancelLoan(idLoan);
        verifyNoMoreInteractions(loanService);
    }
}
