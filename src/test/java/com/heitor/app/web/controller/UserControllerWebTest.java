package com.heitor.app.web.controller;

import com.heitor.app.controller.UserController;
import com.heitor.app.dto.input.UserPatchDTO;
import com.heitor.app.dto.input.UserUpsertDTO;
import com.heitor.app.dto.output.LoanResponseDTO;
import com.heitor.app.dto.output.ReservationResponseDTO;
import com.heitor.app.dto.output.UserResponseDTO;
import com.heitor.app.service.UserService;
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

@WebMvcTest(UserController.class)
public class UserControllerWebTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private UserResponseDTO criarUserResponseDto() {
        return UserResponseDTO.builder()
                .id(1L)
                .name("Nome teste")
                .number("4399999999")
                .email("emailteste@gmail.com")
                .build();
    }

    // Testes para o metodo 'getAllUsers'
    @Test
    public void getAllUsers_deveRetornarOk_quandoNaoHouverFiltros() throws Exception {
        UserResponseDTO userResponseDTO = criarUserResponseDto();

        List<UserResponseDTO> usuarios = List.of(userResponseDTO);

        when(userService.getAllUsers(null, null, null, null, null))
                .thenReturn(usuarios);

        mockMvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Nome teste"))
                .andExpect(jsonPath("$[0].number").value("4399999999"))
                .andExpect(jsonPath("$[0].email").value("emailteste@gmail.com"));

        verify(userService, times(1)).getAllUsers(null, null, null, null, null);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void getAllUsers_deveRetornarOk_quandoNomeForInformado() throws Exception {
        UserResponseDTO userResponseDTO = criarUserResponseDto();

        List<UserResponseDTO> usuarios = List.of(userResponseDTO);

        when(userService.getAllUsers("Nome teste", null, null, null, null))
                .thenReturn(usuarios);

        mockMvc.perform(get("/users")
                        .param("name", "Nome teste")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Nome teste"))
                .andExpect(jsonPath("$[0].number").value("4399999999"))
                .andExpect(jsonPath("$[0].email").value("emailteste@gmail.com"));

        verify(userService).getAllUsers("Nome teste", null, null, null, null);
        verifyNoMoreInteractions(userService);
    }

    // Testes para o metodo 'getUserById'
    @Test
    public void getUserById_deveRetornarOk_quandoIdForValido() throws Exception {
        Long idUser = 1L;

        UserResponseDTO userResponseDTO = criarUserResponseDto();

        when(userService.getUserById(idUser))
                .thenReturn(userResponseDTO);

        mockMvc.perform(get("/users/{id}", idUser)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Nome teste"))
                .andExpect(jsonPath("$.number").value("4399999999"))
                .andExpect(jsonPath("$.email").value("emailteste@gmail.com"));

        verify(userService).getUserById(idUser);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void getUserById_deveRetornarBadRequest_quandoIdNaoForNumerico() throws Exception {
        mockMvc.perform(get("/users/{id}", "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    @Test
    public void getUserById_deveRetornarNotFound_quandoIdNaoForInformadoNaRota() throws Exception {
        mockMvc.perform(get("/users/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verifyNoInteractions(userService);
    }

    // Testes para o metodo 'createUser'
    @Test
    public void createUser_deveRetornarCreated_quandoBodyForValido() throws Exception {
        UserResponseDTO userResponseDTO = criarUserResponseDto();

        String requestJson = """
                {
                    "name": "Nome teste",
                    "number": "4399999999",
                    "email": "emailteste@gmail.com"
                }
                """;

        when(userService.createUser(any(UserUpsertDTO.class)))
                .thenReturn(userResponseDTO);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Nome teste"))
                .andExpect(jsonPath("$.number").value("4399999999"))
                .andExpect(jsonPath("$.email").value("emailteste@gmail.com"));

        verify(userService, times(1)).createUser(any(UserUpsertDTO.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void createUser_deveRetornarBadRequest_quandoRequisicaoEstiverMalFormatada() throws Exception {
        String requestJson = """
                {
                    "name": "Nome teste",
                    "number": "439999999
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    @Test
    public void createUser_deveRetornarBadRequest_quandoBodyEstiverVazio() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    @Test
    public void createUser_deveRetornarBadRequest_quandoEmailForInvalido() throws Exception {
        String requestJson = """
                {
                    "name": "Nome teste",
                    "number": "4399999999",
                    "email": "email invalido"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    @Test
    public void createUser_deveRetornarUnsupportedMediaType_quandoContentTypeForInvalido() throws Exception {
        String requestJson = """
                {
                    "name": "Nome teste",
                    "number": "4399999999",
                    "email": "emailteste@gmail.com"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_XML)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isUnsupportedMediaType());

        verifyNoInteractions(userService);
    }

    // Testes para o metodo 'partiallyUpdateUser'
    @Test
    public void partiallyUpdateUser_deveRetornarOk_quandoBodyForValido() throws Exception {
        Long idUser = 1L;

        UserResponseDTO userResponseDTO = criarUserResponseDto();

        String requestJson = """
                {
                    "name": "Nome teste"
                }
                """;

        when(userService.partiallyUpdateUser(any(UserPatchDTO.class), eq(idUser)))
                .thenReturn(userResponseDTO);

        mockMvc.perform(patch("/users/{id}", idUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Nome teste"))
                .andExpect(jsonPath("$.number").value("4399999999"))
                .andExpect(jsonPath("$.email").value("emailteste@gmail.com"));

        verify(userService).partiallyUpdateUser(any(UserPatchDTO.class), eq(idUser));
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void partiallyUpdateUser_deveRetornarBadRequest_quandoEmailForInvalido() throws Exception {
        Long idUser = 1L;

        String requestJson = """
                {
                    "email": "email invalido"
                }
                """;

        mockMvc.perform(patch("/users/{id}", idUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    @Test
    public void partiallyUpdateUser_deveRetornarBadRequest_quandoBodyEstiverVazio() throws Exception {
        mockMvc.perform(patch("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    // Testes para o metodo 'updateUser'
    @Test
    public void updateUser_deveRetornarOk_quandoBodyForValido() throws Exception {
        Long idUser = 1L;

        UserResponseDTO userResponseDTO = criarUserResponseDto();

        String requestJson = """
                {
                    "name": "Nome teste",
                    "number": "4399999999",
                    "email": "emailteste@gmail.com"
                }
                """;

        when(userService.updateUser(any(UserUpsertDTO.class), eq(idUser)))
                .thenReturn(userResponseDTO);

        mockMvc.perform(put("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idUser))
                .andExpect(jsonPath("$.name").value("Nome teste"))
                .andExpect(jsonPath("$.number").value("4399999999"))
                .andExpect(jsonPath("$.email").value("emailteste@gmail.com"));

        verify(userService).updateUser(any(UserUpsertDTO.class), eq(idUser));
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void updateUser_deveRetornarBadRequest_quandoBodyForInvalido() throws Exception {
        String requestJson = """
                {
                    "name": "Nome teste",
                    "number": "4399999999",
                    "email": "email invalido"
                }
                """;

        mockMvc.perform(put("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    @Test
    public void updateUser_deveRetornarBadRequest_quandoIdNaoForNumerico() throws Exception {
        String requestJson = """
                {
                    "name": "Nome teste",
                    "number": "4399999999",
                    "email": "emailteste@gmail.com"
                }
                """;

        mockMvc.perform(put("/users/{id}", "abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    // Testes para o metodo 'deactivateUser'
    @Test
    public void deactivateUser_deveRetornarNoContent_quandoIdForValido() throws Exception {
        Long idUser = 1L;

        doNothing().when(userService).deactivateUser(idUser);

        mockMvc.perform(patch("/users/{id}/deactivate", idUser))
                .andExpect(status().isNoContent());

        verify(userService).deactivateUser(idUser);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void deactivateUser_deveRetornarBadRequest_quandoIdNaoForNumerico() throws Exception {
        mockMvc.perform(patch("/users/{id}/deactivate", "abc"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    // Testes para o metodo 'activateUser'
    @Test
    public void activateUser_deveRetornarNoContent_quandoIdForValido() throws Exception {
        Long idUser = 1L;

        doNothing().when(userService).activateUser(idUser);

        mockMvc.perform(patch("/users/{id}/activate", idUser))
                .andExpect(status().isNoContent());

        verify(userService).activateUser(idUser);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void activateUser_deveRetornarBadRequest_quandoIdNaoForNumerico() throws Exception {
        mockMvc.perform(patch("/users/{id}/activate", "abc"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    // Testes para o metodo 'getUserLoans'
    @Test
    public void getUserLoans_deveRetornarOk_quandoIdForValido() throws Exception {
        Long idUser = 1L;

        LoanResponseDTO loan = LoanResponseDTO.builder()
                .id(10L)
                .build();

        List<LoanResponseDTO> loans = List.of(loan);

        when(userService.getUserLoans(idUser)).thenReturn(loans);

        mockMvc.perform(get("/users/{id}/loans", idUser)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(10));

        verify(userService).getUserLoans(idUser);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void getUserLoans_deveRetornarBadRequest_quandoIdNaoForNumerico() throws Exception {
        mockMvc.perform(get("/users/{id}/loans", "abc"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    // Testes para o metodo 'getUserReservations'
    @Test
    public void getUserReservations_deveRetornarOk_quandoIdForValido() throws Exception {
        Long idUser = 1L;

        ReservationResponseDTO reservation = ReservationResponseDTO.builder()
                .id(20L)
                .build();

        List<ReservationResponseDTO> reservations = List.of(reservation);

        when(userService.getUserReservations(idUser)).thenReturn(reservations);

        mockMvc.perform(get("/users/{id}/reservations", idUser)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(20));

        verify(userService).getUserReservations(idUser);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void getUserReservations_deveRetornarBadRequest_quandoIdNaoForNumerico() throws Exception {
        mockMvc.perform(get("/users/{id}/reservations", "abc"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }
}
