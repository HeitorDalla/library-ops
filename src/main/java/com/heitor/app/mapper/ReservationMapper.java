package com.heitor.app.mapper;

import com.heitor.app.dto.input.ReservationRequestDTO;
import com.heitor.app.dto.output.ReservationResponseDTO;
import com.heitor.app.entity.Book;
import com.heitor.app.entity.Reservation;
import com.heitor.app.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReservationMapper {
    public List<ReservationResponseDTO> toDtoList(List<Reservation> entities) {
        if (entities.isEmpty()) {
            return List.of();
        }

        return entities.stream()
                .map(this::toDto)
                .toList();
    }

    public ReservationResponseDTO toDto(Reservation entity) {
        if (entity == null) {
            return null;
        }

        return ReservationResponseDTO.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .bookId(entity.getBook().getId())
                .reservationDate(entity.getReservationDate())
                .dueDate(entity.getDueDate())
                .returnDate(entity.getReturnDate())
                .reservationStatus(entity.getReservationStatus())
                .recordStatus(entity.getRecordStatus())
                .build();

    }

    public Reservation toEntity(ReservationRequestDTO dto, User user, Book book) {
        if (dto == null) {
            return null;
        }

        return Reservation.builder()
                .user(user)
                .book(book)
                .build();
    }
}
