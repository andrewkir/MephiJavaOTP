package ru.andrewkir.service;

import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.andrewkir.config.exception.RequestException;
import ru.andrewkir.model.dto.OtpConfigRequest;
import ru.andrewkir.model.entity.Otp;
import ru.andrewkir.model.entity.OtpConfig;
import ru.andrewkir.model.entity.User;
import ru.andrewkir.model.repository.OtpConfigRepository;
import ru.andrewkir.model.repository.OtpRepository;
import ru.andrewkir.model.repository.UserRepository;

import java.util.List;
import java.util.function.Consumer;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final OtpRepository otpRepository;
    private final OtpConfigRepository otpConfigRepository;

    public AdminService(UserRepository userRepository, UserService userService, OtpRepository otpRepository, OtpConfigRepository otpConfigRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.otpRepository = otpRepository;
        this.otpConfigRepository = otpConfigRepository;
    }

    public ResponseEntity<String> changeConfig(OtpConfigRequest otpConfigRequest) {
        OtpConfig otpConfig = otpConfigRepository.findById(0).orElse(new OtpConfig());
        applyIfPresent(otpConfigRequest.getLifetime(), otpConfig::setLifetime);
        applyIfPresent(otpConfigRequest.getLength(), otpConfig::setLength);

        otpConfigRepository.save(otpConfig);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<String> deleteUserById(int id) {
        User user = userService.getById(id);
        List<Otp> otps = otpRepository.findByUserId(user.getId());
        otps.forEach(otpRepository::delete);
        userRepository.delete(user);

        return ResponseEntity.ok().build();
    }

    private <T> void applyIfPresent(T value, Consumer<T> setter) {
        if (value != null) setter.accept(value);
    }
}