package com.heitor.app.scheduler;

import com.heitor.app.entity.Loan;
import com.heitor.app.entity.Reservation;
import com.heitor.app.repository.LoanRepository;
import com.heitor.app.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class StatusScheduler {
    private final LoanRepository loanRepository;
    private final ReservationRepository reservationRepository;

    public StatusScheduler(LoanRepository loanRepository, ReservationRepository reservationRepository) {
        this.loanRepository = loanRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *") 
    public void updateOverdueLoansAndExpiredReservations() {
        LocalDate today = LocalDate.now();

        /* Empréstimos atrasados */
        List<Loan> overdueLoans = loanRepository.findOverdueLoans(today);

        for (Loan loan : overdueLoans) {
            loan.markAsOverdue();
            loanRepository.save(loan);
        }

        /* Reservas expiradas */
        List<Reservation> expiredReservations = reservationRepository.findPendingExpired(today);

        for (Reservation reservation : expiredReservations) {
            reservation.expire();
            reservationRepository.save(reservation);
        }
    }
}
