package com.heitor.app.service.impl;

import com.heitor.app.dto.input.LoanRequestDTO;
import com.heitor.app.dto.output.LoanResponseDTO;
import com.heitor.app.entity.Book;
import com.heitor.app.entity.Fine;
import com.heitor.app.entity.Loan;
import com.heitor.app.entity.User;
import com.heitor.app.enums.*;
import com.heitor.app.exception.BookNotFoundException;
import com.heitor.app.exception.BusinessException;
import com.heitor.app.exception.LoanNotFoundException;
import com.heitor.app.exception.UserNotFoundException;
import com.heitor.app.mapper.LoanMapper;
import com.heitor.app.repository.BookRepository;
import com.heitor.app.repository.LoanRepository;
import com.heitor.app.repository.UserRepository;
import com.heitor.app.service.FineService;
import com.heitor.app.service.LoanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final FineService fineService;
    private final LoanMapper mapper;

    public LoanServiceImpl(UserRepository userRepository,
                           BookRepository bookRepository,
                           LoanRepository loanRepository,
                           FineService fineService,
                           LoanMapper mapper) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
        this.fineService = fineService;
        this.mapper = mapper;
    }

    private List<Loan> findAllLoans (Long userId,
                                     Boolean fine,
                                     LoanStatus loanStatus,
                                     RecordStatus recordStatus) {
        return loanRepository.getAllLoans(
                userId,
                fine,
                loanStatus,
                recordStatus
        );
    }

    private Loan findLoan (Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException(id));
    }

    @Override
    public List<LoanResponseDTO> getAllLoans(Long userId,
                                             Boolean fine,
                                             LoanStatus loanStatus,
                                             RecordStatus recordStatus) {

        List<Loan> loans = findAllLoans(userId, fine, loanStatus, recordStatus);

        return mapper.toDtoList(loans);
    }

    @Override
    public LoanResponseDTO getLoanById(Long id) {
        Loan loan = findLoan(id);

        return mapper.toDto(loan);
    }

    @Transactional
    @Override
    public LoanResponseDTO createLoan(LoanRequestDTO dto) {
        // Verificar se usuario esta ativo
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserId()));
        if (!user.isActive()) {
            throw new BusinessException("User is not allowed to create loans.");
        }

        // Verificar se ja usuario ja possui um emprestimo com alguns dos livros
        for (Long bookId : dto.getBookIds()) {
            boolean alreadyBorrowed = loanRepository.existsByUserIdAndBooks_IdAndLoanStatus(
                    user.getId(),
                    bookId,
                    LoanStatus.OPEN
            );

            if (alreadyBorrowed) {
                throw new BusinessException("User already borrowed this book");
            }
        }

        List<Book> books = new ArrayList<>();

        // Emprestar os livros
        for (Long bookId : dto.getBookIds()) {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new BookNotFoundException(bookId));

            book.borrow();

            bookRepository.save(book);
            books.add(book);
        }

        Loan loan = mapper.toEntity(user, books);

        loan.initialize();

        loan = loanRepository.save(loan);
        return mapper.toDto(loan);
    }

    @Transactional
    @Override
    public LoanResponseDTO returnLoan(Long id) {
        // Verificar emprestimo
        Loan loan = findLoan(id);
        loan.validateCanBeReturned();

        // Devolver os livros
        for (Book book : loan.getBooks()) {
            book.returnBook();

            bookRepository.save(book);
        }

        // Finalizar devolucao do emprestimo
        loan.finish();

        // Regra de criação de multa
        if (loan.getReturnDate().isAfter(loan.getDueDate())) {
            Fine fine = new Fine();
            fine.setLoan(loan);
            fine.setAmount(new BigDecimal("25.00"));
            fine.setFineStatus(FineStatus.OPEN);
            fine.setRecordStatus(RecordStatus.ACTIVE);
            fine.setCreatedDate(LocalDate.now());
            fine.setPaymentDate(null);

            Fine savedFine = fineService.saveFine(fine);
            loan.setFine(savedFine);
        }

        loanRepository.save(loan);
        return mapper.toDto(loan);
    }

    @Transactional
    @Override
    public void cancelLoan(Long id) {
        Loan loan = findLoan(id);
        loan.cancel();

        // Devolver os livros
        for (Book book : loan.getBooks()) {
            book.returnBook();

            bookRepository.save(book);
        }

        loanRepository.save(loan);
    }
}
