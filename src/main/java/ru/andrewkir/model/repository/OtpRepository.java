package ru.andrewkir.model.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.andrewkir.model.OtpStatus;
import ru.andrewkir.model.entity.Otp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OtpRepository extends CrudRepository<Otp, Long> {
    List<Otp> findByUserId(Integer userId);

    @Query("SELECT o FROM Otp o WHERE o.expiresAt < :now AND o.status = :status")
    List<Otp> findByExpiresAtBeforeAndStatus(
            @Param("now") LocalDateTime now,
            @Param("status") OtpStatus status
    );

    @Modifying
    @Query("DELETE FROM Otp o WHERE o.createdAt < :date AND o.status = :status")
    int deleteByCreatedAtBeforeAndStatus(
            @Param("date") LocalDateTime date,
            @Param("status") OtpStatus status
    );
}