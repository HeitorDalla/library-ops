package com.heitor.app.controller;

import com.heitor.app.dto.input.UserPatchDTO;
import com.heitor.app.dto.input.UserUpsertDTO;
import com.heitor.app.dto.output.UserResponseDTO;
import com.heitor.app.service.UserService;
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
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Test
    public void deveCriarUsuarioERetornarCreated() {
        UserUpsertDTO userUpsertDTO = new UserUpsertDTO();
        userUpsertDTO.setName("Teste nome");
        userUpsertDTO.setNumber("43999999999");
        userUpsertDTO.setEmail("testeemail@gmail.com");

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setName("Teste nome");
        userResponseDTO.setNumber("43999999999");
        userResponseDTO.setEmail("testeemail@gmail.com");

        when(userService.createUser(userUpsertDTO))
                .thenReturn(userResponseDTO);

        ResponseEntity<UserResponseDTO> resultado = userController.createUser(userUpsertDTO);

        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals("Teste nome", resultado.getBody().getName());
        assertEquals("43999999999", resultado.getBody().getNumber());
        assertEquals("testeemail@gmail.com", resultado.getBody().getEmail());

        verify(userService).createUser(userUpsertDTO);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void deveAtualizarParcialmenteUsuarioERetornarOk() {
        Long idUser = 1L;

        UserPatchDTO userPatchDTO = new UserPatchDTO();
        userPatchDTO.setName("Teste nome");
        userPatchDTO.setNumber("43999999999");
        userPatchDTO.setEmail("testeemail@gmail.com");

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setName("Teste nome");
        userResponseDTO.setNumber("43999999999");
        userResponseDTO.setEmail("testeemail@gmail.com");

        when(userService.partiallyUpdateUser(userPatchDTO, idUser))
                .thenReturn(userResponseDTO);

        ResponseEntity<UserResponseDTO> resultado = userController.partiallyUpdateUser(userPatchDTO, idUser);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals("Teste nome", resultado.getBody().getName());
        assertEquals("43999999999", resultado.getBody().getNumber());
        assertEquals("testeemail@gmail.com", resultado.getBody().getEmail());

        verify(userService).partiallyUpdateUser(userPatchDTO, idUser);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void deveAtualizarUsuarioERetornarOk() {
        Long idUser = 1L;

        UserUpsertDTO userUpsertDTO = new UserUpsertDTO();
        userUpsertDTO.setName("Teste nome");
        userUpsertDTO.setNumber("43999999999");
        userUpsertDTO.setEmail("testeemail@gmail.com");

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setName("Teste nome");
        userResponseDTO.setNumber("43999999999");
        userResponseDTO.setEmail("testeemail@gmail.com");

        when(userService.updateUser(userUpsertDTO, idUser))
                .thenReturn(userResponseDTO);

        ResponseEntity<UserResponseDTO> resultado = userController.updateUser(userUpsertDTO, idUser);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals("Teste nome", resultado.getBody().getName());
        assertEquals("43999999999", resultado.getBody().getNumber());
        assertEquals("testeemail@gmail.com", resultado.getBody().getEmail());

        verify(userService).updateUser(userUpsertDTO, idUser);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void deveDesativarUsuarioERetornarNoContent() {
        Long idUser = 1L;

        doNothing().when(userService).deactivateUser(idUser);

        ResponseEntity<Void> resultado = userController.deactivateUser(idUser);

        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
        assertNull(resultado.getBody());

        verify(userService).deactivateUser(idUser);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void deveAtivarUsuarioERetornarNoContent() {
        Long idUser = 1L;

        doNothing().when(userService).activateUser(idUser);

        ResponseEntity<Void> resultado = userController.activateUser(idUser);

        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
        assertNull(resultado.getBody());

        verify(userService).activateUser(idUser);
        verifyNoMoreInteractions(userService);
    }
}