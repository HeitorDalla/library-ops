package com.heitor.app.entity;

import com.heitor.app.enums.RecordStatus;
import com.heitor.app.enums.ReservationStatus;
import com.heitor.app.exception.BusinessException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "reservation_date", nullable = false)
    private LocalDate reservationDate;

    @Column(name = "reservation_due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "reservation_return_date")
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status", nullable = false)
    private ReservationStatus reservationStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_record_status", nullable = false)
    private RecordStatus recordStatus;

    public void initialize() {
        reservationDate = LocalDate.now();
        dueDate = reservationDate.plusDays(2);
        reservationStatus = ReservationStatus.PENDING;
        recordStatus = RecordStatus.ACTIVE;
    }

    public void confirm() {
        if (reservationStatus != ReservationStatus.PENDING) {
            throw new BusinessException("Only pending reservations can be confirmed.");
        }

        reservationStatus = ReservationStatus.CONFIRMED;
    }

    public void expire() {
        if (reservationStatus != ReservationStatus.PENDING) {
            throw new BusinessException("Only pending reservations can expire.");
        }

        reservationStatus = ReservationStatus.EXPIRED;
        recordStatus = RecordStatus.INACTIVE;
    }

    public void finish() {
        if (reservationStatus != ReservationStatus.CONFIRMED) {
            throw new BusinessException("Only confirmed reservations can be returned.");
        }

        returnDate = LocalDate.now();
        reservationStatus = ReservationStatus.RETURNED;
        recordStatus = RecordStatus.INACTIVE;
    }

    public void cancel() {
        if (reservationStatus == ReservationStatus.CANCELLED) {
            throw new BusinessException("Reservation already cancelled.");
        }
        if (reservationStatus == ReservationStatus.EXPIRED) {
            throw new BusinessException("Expired reservations cannot be cancelled.");
        }

        this.reservationStatus = ReservationStatus.CANCELLED;
        this.recordStatus = RecordStatus.INACTIVE;
    }

    public boolean holdsBook() {
        return reservationStatus == ReservationStatus.CONFIRMED;
    }
}