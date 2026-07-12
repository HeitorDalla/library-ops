package com.heitor.app.mapper;

import com.heitor.app.dto.output.FineResponseDTO;
import com.heitor.app.entity.Fine;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FineMapper {
    public List<FineResponseDTO> toDtoList(List<Fine> entities) {
        if (entities.isEmpty()) {
            return List.of();
        }

        return entities.stream()
                .map(this::toDto)
                .toList();
    }

    public FineResponseDTO toDto(Fine entity) {
        if (entity == null) {
            return null;
        }

        return FineResponseDTO.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .fineStatus(entity.getFineStatus())
                .recordStatus(entity.getRecordStatus())
                .createdDate(entity.getCreatedDate())
                .paymentDate(entity.getPaymentDate())
                .loanId(entity.getLoan().getId())
                .build();
    }
}
