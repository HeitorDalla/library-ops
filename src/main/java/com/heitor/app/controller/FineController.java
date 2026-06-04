package com.heitor.app.controller;

import com.heitor.app.dto.output.FineResponseDTO;
import com.heitor.app.enums.FineStatus;
import com.heitor.app.enums.RecordStatus;
import com.heitor.app.service.FineService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/fines")
public class FineController {
    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<FineResponseDTO>> getAllFines(
            @RequestParam(required = false) BigDecimal amount,
            @RequestParam(required = false) FineStatus fineStatus,
            @RequestParam(required = false) RecordStatus recordStatus) {

        return ResponseEntity.ok(fineService.getAllFines(
                amount,
                fineStatus,
                recordStatus
        ));
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<FineResponseDTO> getFineById(@PathVariable Long id) {
        return ResponseEntity.ok(fineService.getFineById(id));
    }

    @PatchMapping(
            path = "/{id}/payment",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<FineResponseDTO> payFine(@PathVariable Long id) {
        return ResponseEntity.ok(fineService.payFine(id));
    }

    @PatchMapping(
            path = "/{id}/cancel",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> cancelFine(@PathVariable Long id) {
        fineService.cancelFine(id);

        return ResponseEntity.noContent().build();
    }
}
