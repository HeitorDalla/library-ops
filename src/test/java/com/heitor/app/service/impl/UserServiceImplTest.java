package com.heitor.app.service.impl;

import com.heitor.app.dto.input.UserPatchDTO;
import com.heitor.app.dto.input.UserUpsertDTO;
import com.heitor.app.dto.output.UserResponseDTO;
import com.heitor.app.entity.User;
import com.heitor.app.enums.*;
import com.heitor.app.exception.BusinessException;
import com.heitor.app.exception.UserNotFoundException;
import com.heitor.app.repository.FineRepository;
import com.heitor.app.repository.LoanRepository;
import com.heitor.app.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.heitor.app.mapper.UserMapper;
import com.heitor.app.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private FineRepository fineRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserMapper userMapper;

    @Test
    public void deveCriarUsuarioQuandoDadosValidos() {
        UserUpsertDTO userUpsertDTO = new UserUpsertDTO();
        userUpsertDTO.setName("Teste nome");
        userUpsertDTO.setNumber("439999999999");
        userUpsertDTO.setEmail("testeemail@gmail.com");

        User user = new User();
        user.setName("Teste nome");
        user.setNumber("439999999999");
        user.setEmail("testeemail@gmail.com");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Teste nome");
        savedUser.setNumber("439999999999");
        savedUser.setEmail("testeemail@gmail.com");

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setName("Teste nome");
        userResponseDTO.setNumber("439999999999");
        userResponseDTO.setEmail("testeemail@gmail.com");

        when(userMapper.toEntity(userUpsertDTO))
                .thenReturn(user);
        when(userRepository.save(user))
                .thenReturn(savedUser);
        when(userMapper.toDto(savedUser))
                .thenReturn(userResponseDTO);

        UserResponseDTO result = userServiceImpl.createUser(userUpsertDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Teste nome", result.getName());
        assertEquals("439999999999", result.getNumber());
        assertEquals("testeemail@gmail.com", result.getEmail());

        assertNotNull(user.getRegistrationDate());
        assertEquals(UserStatus.OK, user.getUserStatus());
        assertEquals(RecordStatus.ACTIVE, user.getRecordStatus());
        assertTrue(user.isActive());

        verify(userMapper)
                .toEntity(userUpsertDTO);
        verify(userRepository)
                .save(user);
        verify(userMapper)
                .toDto(savedUser);
    }

    @Test
    public void atualizacaoParcialUsuarioQuandoNomeNumeroEmailInformado() {
        Long idUser = 1L;

        User user = new User();
        user.setId(idUser);
        user.setName("Nome antigo");
        user.setNumber("438888888888");
        user.setEmail("emailantigo@gmail.com");

        UserPatchDTO userPatchDTO = new UserPatchDTO();
        userPatchDTO.setName("Teste nome");
        userPatchDTO.setNumber("439999999999");
        userPatchDTO.setEmail("testeemail@gmail.com");

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(idUser);
        userResponseDTO.setName("Teste nome");
        userResponseDTO.setNumber("439999999999");
        userResponseDTO.setEmail("testeemail@gmail.com");

        when(userRepository.findById(idUser))
                .thenReturn(Optional.of(user));
        when(userMapper.toDto(user))
                .thenReturn(userResponseDTO);

        UserResponseDTO resultado = userServiceImpl.partiallyUpdateUser(userPatchDTO, idUser);

        assertNotNull(resultado);
        assertEquals(idUser, resultado.getId());
        assertEquals("Teste nome", resultado.getName());
        assertEquals("439999999999", resultado.getNumber());
        assertEquals("testeemail@gmail.com", resultado.getEmail());

        verify(userRepository)
                .findById(idUser);
        verify(userMapper)
                .patchEntity(userPatchDTO, user);
        verify(userRepository)
                .save(user);
        verify(userMapper)
                .toDto(user);
    }

    @Test
    public void deveLancarExcecaoQuandoAtualizarParcialmenteUsuarioInexistente() {
        Long idUser = 1L;

        UserPatchDTO dto = new UserPatchDTO();

        when(userRepository.findById(idUser))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
                userServiceImpl.partiallyUpdateUser(dto, idUser);
        });

        verify(userRepository)
                .findById(idUser);
        verify(userMapper, never())
                .patchEntity(any(), any());
        verify(userRepository, never())
                .save(any());
    }
 
    @Test
    public void atualizacaoParcialUsuarioQuandoNomeNumeroInformado() {
        Long idUser = 1L;

        User user = new User();
        user.setId(idUser);
        user.setName("Nome antigo");
        user.setNumber("438888888888");
        user.setEmail("emailantigo@gmail.com");

        UserPatchDTO userPatchDTO = new UserPatchDTO();
        userPatchDTO.setName("Teste nome");
        userPatchDTO.setNumber("439999999999");

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(idUser);
        userResponseDTO.setName("Teste nome");
        userResponseDTO.setNumber("439999999999");
        userResponseDTO.setEmail("emailantigo@gmail.com");

        when(userRepository.findById(idUser))
                .thenReturn(Optional.of(user));
        when(userMapper.toDto(user))
                .thenReturn(userResponseDTO);

        UserResponseDTO resultado = userServiceImpl.partiallyUpdateUser(userPatchDTO, idUser);

        assertNotNull(resultado);
        assertEquals(idUser, resultado.getId());
        assertEquals("Teste nome", resultado.getName());
        assertEquals("439999999999", resultado.getNumber());
        assertEquals("emailantigo@gmail.com", resultado.getEmail());

        verify(userRepository)
                .findById(idUser);
        verify(userMapper)
                .patchEntity(userPatchDTO, user);
        verify(userRepository)
                .save(user);
        verify(userMapper)
                .toDto(user);
    }

    @Test
    public void atualizacaoTotalUsuarioQuandoNomeNumeroEmailInformado() {
        Long idUser = 1L;

        User user = new User();
        user.setId(idUser);
        user.setName("Nome antigo");
        user.setNumber("438888888888");
        user.setEmail("emailantigo@gmail.com");

        UserUpsertDTO userUpsertDTO = new UserUpsertDTO();
        userUpsertDTO.setName("Teste nome");
        userUpsertDTO.setNumber("439999999999");
        userUpsertDTO.setEmail("testeemail@gmail.com");

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(idUser);
        userResponseDTO.setName("Teste nome");
        userResponseDTO.setNumber("439999999999");
        userResponseDTO.setEmail("testeemail@gmail.com");

        when(userRepository.findById(idUser))
                .thenReturn(Optional.of(user));
        when(userMapper.toDto(user))
                .thenReturn(userResponseDTO);

        UserResponseDTO resultado = userServiceImpl.updateUser(userUpsertDTO, idUser);

        assertNotNull(resultado);
        assertEquals(idUser, resultado.getId());
        assertEquals("Teste nome", resultado.getName());
        assertEquals("439999999999", resultado.getNumber());
        assertEquals("testeemail@gmail.com", resultado.getEmail());

        verify(userRepository)
                .findById(idUser);
        verify(userMapper)
                .updateEntity(userUpsertDTO, user);
        verify(userRepository)
                .save(user);
        verify(userMapper)
                .toDto(user);
    }

    @Test
    public void deveLancarExcecaoQuandoAtualizarUsuarioInexistente() {
        Long idUser = 1L;

        UserUpsertDTO dto = new UserUpsertDTO();

        when(userRepository.findById(idUser))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
                userServiceImpl.updateUser(dto, idUser);
        });

        verify(userRepository)
                .findById(idUser);
        verify(userMapper, never())
                .updateEntity(any(), any());
        verify(userRepository, never())
                .save(any());
    }

    @Test
    public void ativarUsuarioQuandoUsuarioExiste() {
        Long idUser = 1L;

        User user = new User();
        user.setId(idUser);
        user.setUserStatus(null);
        user.setRecordStatus(RecordStatus.INACTIVE);

        when(userRepository.findById(idUser))
                .thenReturn(Optional.of(user));

        userServiceImpl.activateUser(idUser);

        assertEquals(UserStatus.OK, user.getUserStatus());
        assertEquals(RecordStatus.ACTIVE, user.getRecordStatus());
        assertTrue(user.isActive());

        verify(userRepository)
                .findById(idUser);
        verify(userRepository)
                .save(user);
    }

    @Test
    public void deveLancarExcecaoQuandoAtivarUsuarioInexistente() {
        Long idUser = 1L;

        when(userRepository.findById(idUser))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
                userServiceImpl.activateUser(idUser);
        });

        verify(userRepository)
                .findById(idUser);
        verify(userRepository, never())
                .save(any());
    }

    @Test
    public void desativarUsuarioQuandoNaoPossuiPendencias() {
        Long idUser = 1L;

        User user = new User();
        user.setId(idUser);
        user.setUserStatus(UserStatus.OK);
        user.setRecordStatus(RecordStatus.ACTIVE);

        when(userRepository.findById(idUser))
                .thenReturn(Optional.of(user));
        when(loanRepository.existsByUserAndLoanStatus(user, List.of(LoanStatus.OPEN, LoanStatus.OVERDUE)))
                .thenReturn(false);
        when(fineRepository.existsByLoanUserAndFineStatus(user, FineStatus.OPEN))
                .thenReturn(false);
        when(reservationRepository.existsByUserAndReservationStatus(user, List.of(ReservationStatus.PENDING, ReservationStatus.EXPIRED)))
                .thenReturn(false);

        userServiceImpl.deactivateUser(idUser);

        assertEquals(UserStatus.OK, user.getUserStatus());
        assertEquals(RecordStatus.INACTIVE, user.getRecordStatus());
        assertFalse(user.isActive());

        verify(userRepository)
                .findById(idUser);
        verify(loanRepository)
                .existsByUserAndLoanStatus(user, List.of(LoanStatus.OPEN, LoanStatus.OVERDUE));
        verify(fineRepository)
                .existsByLoanUserAndFineStatus(user, FineStatus.OPEN);
        verify(reservationRepository)
                .existsByUserAndReservationStatus(user, List.of(ReservationStatus.PENDING, ReservationStatus.EXPIRED));
        verify(userRepository)
                .save(user);
    }

    @Test
    public void naoDesativarQuandoPossuiEmprestimosAtivos() {
        Long idUser = 1L;

        User user = new User();
        user.setId(idUser);

        when(userRepository.findById(idUser))
                .thenReturn(Optional.of(user));
        when(loanRepository.existsByUserAndLoanStatus(user, List.of(LoanStatus.OPEN, LoanStatus.OVERDUE)))
                .thenReturn(true);
        assertThrows(BusinessException.class, () -> {
            userServiceImpl.deactivateUser(idUser);
        });

        verify(userRepository)
                .findById(idUser);
        verify(loanRepository)
                .existsByUserAndLoanStatus(user, List.of(LoanStatus.OPEN, LoanStatus.OVERDUE));
        verify(fineRepository, never())
                .existsByLoanUserAndFineStatus(user, FineStatus.OPEN);
        verify(reservationRepository, never())
                .existsByUserAndReservationStatus(user, List.of(ReservationStatus.PENDING, ReservationStatus.EXPIRED));
        verify(userRepository, never())
                .save(any());
    }

    @Test
    public void naoDesativarQuandoPossuiMultasAtivas() {
        Long idUser = 1L;

        User user = new User();
        user.setId(idUser);

        when(userRepository.findById(idUser))
                .thenReturn(Optional.of(user));
        when(loanRepository.existsByUserAndLoanStatus(user, List.of(LoanStatus.OPEN, LoanStatus.OVERDUE)))
                .thenReturn(false);
        when(fineRepository.existsByLoanUserAndFineStatus(user, FineStatus.OPEN))
                .thenReturn(true);

        assertThrows(BusinessException.class, () -> {
            userServiceImpl.deactivateUser(idUser);
        });

        verify(userRepository)
                .findById(idUser);
        verify(loanRepository)
                .existsByUserAndLoanStatus(user, List.of(LoanStatus.OPEN, LoanStatus.OVERDUE));
        verify(fineRepository)
                .existsByLoanUserAndFineStatus(user, FineStatus.OPEN);
        verify(reservationRepository, never())
                .existsByUserAndReservationStatus(user, List.of(ReservationStatus.PENDING, ReservationStatus.EXPIRED));
        verify(userRepository, never())
                .save(any());
    }

    @Test
    public void naoDesativarQuandoPossuiReservasAtivas() {
        Long idUser = 1L;

        User user = new User();
        user.setId(idUser);

        when(userRepository.findById(idUser))
                .thenReturn(Optional.of(user));
        when(loanRepository.existsByUserAndLoanStatus(user, List.of(LoanStatus.OPEN, LoanStatus.OVERDUE)))
                .thenReturn(false);
        when(fineRepository.existsByLoanUserAndFineStatus(user, FineStatus.OPEN))
                .thenReturn(false);
        when(reservationRepository.existsByUserAndReservationStatus(user, List.of(ReservationStatus.PENDING, ReservationStatus.EXPIRED)))
                .thenReturn(true);

        assertThrows(BusinessException.class, () -> {
            userServiceImpl.deactivateUser(idUser);
        });

        verify(userRepository)
                .findById(idUser);
        verify(loanRepository)
                .existsByUserAndLoanStatus(user, List.of(LoanStatus.OPEN, LoanStatus.OVERDUE));
        verify(fineRepository)
                .existsByLoanUserAndFineStatus(user, FineStatus.OPEN);
        verify(reservationRepository)
                .existsByUserAndReservationStatus(user, List.of(ReservationStatus.PENDING, ReservationStatus.EXPIRED));
        verify(userRepository, never())
                .save(any());
    }

    @Test
    public void deveLancarExcecaoQuandoDesativarUsuarioInexistente() {
        Long idUser = 1L;

        when(userRepository.findById(idUser))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
                userServiceImpl.deactivateUser(idUser);
        });

        verify(userRepository)
                .findById(idUser);
        verify(userRepository, never())
                .save(any());
    }
}