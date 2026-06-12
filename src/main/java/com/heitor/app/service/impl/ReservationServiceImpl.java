package com.heitor.app.service.impl;

import com.heitor.app.dto.input.ReservationRequestDTO;
import com.heitor.app.dto.output.ReservationResponseDTO;
import com.heitor.app.entity.Book;
import com.heitor.app.entity.Reservation;
import com.heitor.app.entity.User;
import com.heitor.app.enums.*;
import com.heitor.app.exception.*;
import com.heitor.app.integration.repository.*;
import com.heitor.app.mapper.ReservationMapper;
import com.heitor.app.repository.BookRepository;
import com.heitor.app.repository.FineRepository;
import com.heitor.app.repository.ReservationRepository;
import com.heitor.app.repository.UserRepository;
import com.heitor.app.service.ReservationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final FineRepository fineRepository;
    private final ReservationMapper reservationMapper;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  UserRepository userRepository,
                                  BookRepository bookRepository,
                                  FineRepository fineRepository,
                                  ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.fineRepository = fineRepository;
        this.reservationMapper = reservationMapper;
    }

    private List<Reservation> findAllReservations(Long userId,
                                                  Long bookId,
                                                  ReservationStatus reservationStatus,
                                                  RecordStatus recordStatus) {
        return reservationRepository.findAllReservations(userId, bookId, reservationStatus, recordStatus);
    }

    private Reservation findReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReserveNotFoundException(id));
    }

    private void tryConfirmNextReservation(Book book) {
        if (book.getAvailableQuantity() <= 0) {
            return;
        }

        reservationRepository.findFirstByBookAndReservationStatusOrderByReservationDateAscIdAsc(book, ReservationStatus.PENDING)
                .ifPresent(reservation -> {
                    reservation.confirm();
                    book.reserve();

                    reservationRepository.save(reservation);
                    bookRepository.save(book);
                });
    }

    @Override
    public List<ReservationResponseDTO> getAllReservations(Long userId,
                                                           Long bookId,
                                                           ReservationStatus reservationStatus,
                                                           RecordStatus recordStatus) {

        List<Reservation> reservations = findAllReservations(userId, bookId, reservationStatus, recordStatus);

        return reservationMapper.toDtoList(reservations);
    }

    @Override
    public ReservationResponseDTO getReservationById(Long id) {
        Reservation reservation = findReservation(id);

        return reservationMapper.toDto(reservation);
    }

    @Transactional
    @Override
    public ReservationResponseDTO createReservation(ReservationRequestDTO dto) {
        // Verificar usuario esta ativo
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserId()));
        if (!user.isActive()) {
            throw new BusinessException("User is not allowed to create reservations.");
        }

        // Verificar se usuário tem multa pendente
        if (fineRepository.existsByLoanUserAndFineStatus(user, FineStatus.OPEN)) {
            throw new BusinessException("User has an outstanding fine.");
        }

        // Verificar livro
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new BookNotFoundException(dto.getBookId()));
        if (book.getRecordStatus() != RecordStatus.ACTIVE) {
            throw new BusinessException("Book is inactive.");
        }

        // Impedir reserva duplicada do livro pelo mesmo usuário
        boolean exists = reservationRepository.
                existsByUserAndBookAndReservationStatus(
                        user,
                        book,
                        List.of(
                                ReservationStatus.PENDING,
                                ReservationStatus.CONFIRMED,
                                ReservationStatus.EXPIRED)
                );

        if (exists) {
            throw new BusinessException("User already has a reservation for this book");
        }

        Reservation reservation = reservationMapper.toEntity(dto, user, book);
        reservation.initialize();
        reservationRepository.save(reservation);

        // tenta confirmar caso exista estoque disponível
        tryConfirmNextReservation(book);

        return reservationMapper.toDto(reservation);
    }

    @Transactional
    @Override
    public ReservationResponseDTO returnReservation(Long id) {
        Reservation reservation = findReservation(id);
        Book book = reservation.getBook();

        reservation.finish();
        book.releaseReservation();

        reservationRepository.save(reservation);
        bookRepository.save(book);

        // chama a próxima pessoa da fila
        tryConfirmNextReservation(book);

        return reservationMapper.toDto(reservation);
    }

    @Transactional
    @Override
    public void cancelReservation(Long id) {
        Reservation reservation = findReservation(id);
        Book book = reservation.getBook();

        // verifica se a reserva esta confirmada
        boolean wasHolding = reservation.holdsBook();

        reservation.cancel();
        reservationRepository.save(reservation);

        if (wasHolding) {
            book.releaseReservation();
            bookRepository.save(book);

            tryConfirmNextReservation(book);
        }
    }
}
