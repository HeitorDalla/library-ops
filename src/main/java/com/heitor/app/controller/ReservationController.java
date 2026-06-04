package com.heitor.app.controller;

import com.heitor.app.dto.input.ReservationRequestDTO;
import com.heitor.app.dto.output.ReservationResponseDTO;
import com.heitor.app.enums.RecordStatus;
import com.heitor.app.enums.ReservationStatus;
import com.heitor.app.service.ReservationService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) ReservationStatus reservationStatus,
            @RequestParam(required = false) RecordStatus recordStatus) {

        return ResponseEntity.ok(reservationService.getAllReservations(
                userId,
                bookId,
                reservationStatus,
                recordStatus)
        );
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ReservationResponseDTO> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ReservationResponseDTO> createReservation(@Valid @RequestBody ReservationRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.createReservation(dto));
    }

    @PatchMapping(
            path = "/{id}/return",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ReservationResponseDTO> returnReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.returnReservation(id));
    }

    @PatchMapping(
            path = "/{id}/cancel",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ReservationResponseDTO> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);

        return ResponseEntity.noContent().build();
    }
}
