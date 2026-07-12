package com.heitor.app.entity;

import com.heitor.app.enums.RecordStatus;
import com.heitor.app.enums.UserStatus;
import com.heitor.app.exception.BusinessException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
// @Cacheable  // anotacao para a implementacao do cache nivel 2 do hibernate
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false)
    private String name;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_number", nullable = false)
    private String number;

    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    @Column(name = "user_registration_date", nullable = false)
    private LocalDate registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_record_status", nullable = false)
    private RecordStatus recordStatus;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Loan> loans = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();

    public void activate() {
        userStatus = UserStatus.OK;
        recordStatus = RecordStatus.ACTIVE;
    }

    public void deactivate() {
        recordStatus = RecordStatus.INACTIVE;
    }

    public void initialize() {
        if (id != null) {
            throw new BusinessException("User already initialized.");
        }

        registrationDate = LocalDate.now();
        activate();
    }

    public boolean isActive() {
        return userStatus == UserStatus.OK && recordStatus == RecordStatus.ACTIVE;
    }
}