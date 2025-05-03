package ru.andrewkir.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.andrewkir.model.OtpStatus;
import ru.andrewkir.model.entity.Otp;
import ru.andrewkir.model.repository.OtpRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpCleanupService {

    private final OtpRepository otpRepository;

    /**
     * Очистка просроченных OTP каждые 30 минут
     */
    @Scheduled(fixedRate = 30 * 60 * 1000)
    @Transactional
    public void cleanupExpiredOtps() {
        LocalDateTime now = LocalDateTime.now();
        List<Otp> expiredOtps = otpRepository.findByExpiresAtBeforeAndStatus(
                now,
                OtpStatus.Expired
        );

        if (!expiredOtps.isEmpty()) {
            log.info("Найдено {} просроченных OTP для очистки", expiredOtps.size());

            expiredOtps.forEach(otp -> {
                otp.setStatus(OtpStatus.Expired);
                log.debug("Помечен как просроченный OTP с ID: {}", otp.getId());
            });

            otpRepository.saveAll(expiredOtps);
            log.info("Успешно обновлено {} OTP", expiredOtps.size());
        }
    }

    /**
     * Полная очистка старых записей (старше 7 дней) раз в сутки
     */
    @Scheduled(cron = "0 0 3 * * ?") // Каждый день в 3:00
    @Transactional
    public void cleanupOldOtps() {
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        int deletedCount = otpRepository.deleteByCreatedAtBeforeAndStatus(
                weekAgo,
                OtpStatus.Expired
        );

        log.info("Удалено {} старых OTP записей", deletedCount);
    }
}