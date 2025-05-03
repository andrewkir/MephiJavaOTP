package ru.andrewkir.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.andrewkir.config.exception.RequestException;
import ru.andrewkir.model.OtpStatus;
import ru.andrewkir.model.dto.OtpRequest;
import ru.andrewkir.model.dto.OtpResponse;
import ru.andrewkir.model.dto.OtpValidateResponse;
import ru.andrewkir.model.entity.Otp;
import ru.andrewkir.model.entity.OtpConfig;
import ru.andrewkir.model.entity.User;
import ru.andrewkir.model.repository.OtpConfigRepository;
import ru.andrewkir.model.repository.OtpRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OtpService {
    private final UserService userService;
    private final OtpRepository otpRepository;
    private final OtpConfigRepository otpConfigRepository;
    private final CommunicationService communicationService;

    public OtpService(UserService userService, OtpRepository otpRepository, OtpConfigRepository otpConfigRepository, CommunicationService communicationService) {
        this.userService = userService;
        this.otpRepository = otpRepository;
        this.otpConfigRepository = otpConfigRepository;
        this.communicationService = communicationService;
    }

    public OtpResponse generateOtp(OtpRequest otpRequest) {
        User user = userService.getCurrentUser();
        OtpConfig otpConfig = otpConfigRepository.findById(0).orElseThrow(() -> new RequestException("Отсутствует конфигурация"));
        if (otpConfig.getLifetime() <= 0 || otpConfig.getLength() <= 0) {
            throw new RequestException("Неправильные настройки конфига");
        }

        Otp otp = new Otp(generateCode(otpConfig.getLength()), user, otpRequest.getOperationId(), LocalDateTime.now().plusSeconds(otpConfig.getLifetime()), LocalDateTime.now());
        otpRepository.save(otp);
        communicationService.handleSendOtp(otp, user.getOtpDestination(), user.getAddress());
        return new OtpResponse(otp.getValue(), otp.getExpiresAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
    }

    public ResponseEntity<OtpValidateResponse> validateOtp(String value, String operationId) {
        User user = userService.getCurrentUser();
        try {
            List<Otp> otpList = otpRepository.findByUserId(user.getId());
            Otp result = otpList.stream().filter(otp ->
                            otp.getValue().equals(value)
                                    && otp.getOperationId().equals(operationId)
                                    && otp.getStatus() == OtpStatus.Active
                                    && otp.getExpiresAt().isAfter(LocalDateTime.now()))
                    .findFirst().orElse(null);
            if (result != null) {
                result.setStatus(OtpStatus.Used);
                otpRepository.save(result);
                return ResponseEntity.ok().body(new OtpValidateResponse(true));
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(new OtpValidateResponse(false));
        }

        return ResponseEntity.status(404).body(new OtpValidateResponse(false));
    }

    public static String generateCode(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
