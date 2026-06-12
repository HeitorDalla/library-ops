package com.heitor.app.unit.service.impl;

import com.heitor.app.dto.output.FineResponseDTO;
import com.heitor.app.entity.Fine;
import com.heitor.app.entity.Loan;
import com.heitor.app.enums.FineStatus;
import com.heitor.app.enums.RecordStatus;
import com.heitor.app.exception.BusinessException;
import com.heitor.app.exception.FineNotFoundException;
import com.heitor.app.mapper.FineMapper;
import com.heitor.app.repository.FineRepository;
import com.heitor.app.service.impl.FineServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FineServiceImplTest {

    @InjectMocks
    private FineServiceImpl fineServiceImpl;

    @Mock
    private FineRepository fineRepository;

    @Mock
    private FineMapper fineMapper;

    @Test
    public void deveSalvarMultaQuandoPossuiEmprestimoAssociado() {
        Loan loan = new Loan();

        Fine fine = new Fine();
        fine.setLoan(loan);

        Fine savedFine = new Fine();
        savedFine.setId(1L);

        when(fineRepository.save(fine))
                .thenReturn(savedFine);

        Fine result = fineServiceImpl.saveFine(fine);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(fineRepository)
                .save(fine);
    }

    @Test
    public void naoDeveSalvarMultaQuandoNaoPossuiEmprestimoAssociado() {
        Fine fine = new Fine();

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> fineServiceImpl.saveFine(fine)
        );

        assertEquals(
                "Fine must be associated with a loan.",
                exception.getMessage()
        );

        verify(fineRepository, never())
                .save(any());
    }

    @Test
    public void devePagarMultaQuandoMultaEstaAberta() {
        Long idFine = 1L;

        Fine fine = new Fine();
        fine.setId(idFine);
        fine.setFineStatus(FineStatus.OPEN);
        fine.setRecordStatus(RecordStatus.ACTIVE);

        FineResponseDTO responseDTO = new FineResponseDTO();
        responseDTO.setId(idFine);

        when(fineRepository.findById(idFine))
                .thenReturn(Optional.of(fine));
        when(fineMapper.toDto(fine))
                .thenReturn(responseDTO);

        FineResponseDTO result = fineServiceImpl.payFine(idFine);

        assertNotNull(result);
        assertEquals(idFine, result.getId());
        assertEquals(FineStatus.PAID, fine.getFineStatus());
        assertEquals(RecordStatus.INACTIVE, fine.getRecordStatus());
        assertNotNull(fine.getPaymentDate());

        verify(fineRepository)
                .findById(idFine);
        verify(fineRepository)
                .save(fine);
        verify(fineMapper)
                .toDto(fine);
    }

    @Test
    public void naoDevePagarMultaQuandoMultaNaoEstaAberta() {
        Long idFine = 1L;

        Fine fine = new Fine();
        fine.setId(idFine);
        fine.setFineStatus(FineStatus.PAID);
        fine.setRecordStatus(RecordStatus.INACTIVE);

        when(fineRepository.findById(idFine))
                .thenReturn(Optional.of(fine));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> fineServiceImpl.payFine(idFine)
        );

        assertEquals(
                "Only open fines can be paid.",
                exception.getMessage()
        );
        assertNull(fine.getPaymentDate());
        assertEquals(FineStatus.PAID, fine.getFineStatus());
        assertEquals(RecordStatus.INACTIVE, fine.getRecordStatus());

        verify(fineRepository)
                .findById(idFine);
        verify(fineRepository, never())
                .save(any());
        verify(fineMapper, never())
                .toDto(any());
    }

    @Test
    public void deveCancelarMultaQuandoMultaEstaAberta() {
        Long idFine = 1L;

        Fine fine = new Fine();
        fine.setId(idFine);
        fine.setFineStatus(FineStatus.OPEN);
        fine.setRecordStatus(RecordStatus.ACTIVE);

        when(fineRepository.findById(idFine))
                .thenReturn(Optional.of(fine));

        fineServiceImpl.cancelFine(idFine);

        assertEquals(FineStatus.CANCELLED, fine.getFineStatus());
        assertEquals(RecordStatus.INACTIVE, fine.getRecordStatus());

        verify(fineRepository)
                .findById(idFine);
        verify(fineRepository)
                .save(fine);
    }

    @Test
    public void naoDeveCancelarMultaQuandoMultaNaoEstaAberta() {
        Long idFine = 1L;

        Fine fine = new Fine();
        fine.setId(idFine);
        fine.setFineStatus(FineStatus.PAID);

        when(fineRepository.findById(idFine))
                .thenReturn(Optional.of(fine));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> fineServiceImpl.cancelFine(idFine)
        );

        assertEquals(
                "Only open fines can be cancelled.",
                exception.getMessage()
        );

        verify(fineRepository)
                .findById(idFine);
        verify(fineRepository, never())
                .save(any());
    }

    @Test
    public void deveLancarExcecaoQuandoPagarMultaInexistente() {
        Long idFine = 1L;

        when(fineRepository.findById(idFine))
                .thenReturn(Optional.empty());

        assertThrows(FineNotFoundException.class,
                () -> fineServiceImpl.payFine(idFine));

        verify(fineRepository)
                .findById(idFine);
        verify(fineRepository, never())
                .save(any());
        verify(fineMapper, never())
                .toDto(any());
    }

    @Test
    public void deveLancarExcecaoQuandoCancelarMultaInexistente() {
        Long idFine = 1L;

        when(fineRepository.findById(idFine))
                .thenReturn(Optional.empty());

        assertThrows(FineNotFoundException.class,
                () -> fineServiceImpl.cancelFine(idFine));

        verify(fineRepository)
                .findById(idFine);
        verify(fineRepository, never())
                .save(any());
    }
}