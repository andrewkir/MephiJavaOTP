package ru.andrewkir.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import ru.andrewkir.model.OtpStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp")
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Getter
    private int id;

    @Column
    @Getter
    private String value;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @Getter
    private User user;

    @Column(name = "operation_id")
    @Getter
    private String operationId;

    @Column(name = "status")
    @Getter
    @Setter
    private OtpStatus status = OtpStatus.Active;

    @Getter
    @Column(name = "expires_at", updatable = false)
    private LocalDateTime expiresAt;

    @CreatedDate
    @Getter
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    public Otp(String value, User user, String operationId, LocalDateTime expiresAt, LocalDateTime createdAt) {
        this.value = value;
        this.user = user;
        this.operationId = operationId;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public Otp() {
    }
}
