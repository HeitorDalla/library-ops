package com.heitor.app.controller;

import com.heitor.app.dto.input.UserPatchDTO;
import com.heitor.app.dto.input.UserUpsertDTO;
import com.heitor.app.dto.output.LoanResponseDTO;
import com.heitor.app.dto.output.ReservationResponseDTO;
import com.heitor.app.dto.output.UserResponseDTO;
import com.heitor.app.enums.RecordStatus;
import com.heitor.app.enums.UserStatus;
import com.heitor.app.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String number,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) UserStatus userStatus,
            @RequestParam(required = false) RecordStatus recordStatus) {

        return ResponseEntity.ok(userService.getAllUsers(
                name,
                number,
                email,
                userStatus,
                recordStatus
        ));
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserUpsertDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
    }

    @PatchMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserResponseDTO> partiallyUpdateUser(@Valid @RequestBody UserPatchDTO dto,
                                                               @PathVariable Long id) {
        return ResponseEntity.ok(userService.partiallyUpdateUser(dto, id));
    }

    @PutMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @RequestBody UserUpsertDTO dto,
                                                      @PathVariable Long id) {
        return ResponseEntity.ok(userService.updateUser(dto, id));
    }

    // Rotas para Ativar e Desativar Usuários
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long id) {
        userService.activateUser(id);

        return ResponseEntity.noContent().build();
    }

    // Rotas de relacionamentos
    @GetMapping(
            path = "/{id}/loans",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<LoanResponseDTO>> getUserLoans(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserLoans(id));
    }

    @GetMapping(
            path = "/{id}/reservations", 
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<ReservationResponseDTO>> getUserReservations(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserReservations(id));
    }
}
