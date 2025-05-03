package ru.andrewkir.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.andrewkir.model.UserRoles;
import ru.andrewkir.model.dto.OtpConfigRequest;
import ru.andrewkir.model.entity.OtpConfig;
import ru.andrewkir.model.entity.User;
import ru.andrewkir.model.repository.OtpConfigRepository;
import ru.andrewkir.model.repository.UserRepository;
import ru.andrewkir.service.AdminService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {

    private final UserRepository userRepository;
    private final AdminService adminService;
    private final OtpConfigRepository otpConfigRepository;

    public AdminController(UserRepository userRepository, AdminService adminService, OtpConfigRepository otpConfigRepository) {
        this.userRepository = userRepository;
        this.adminService = adminService;
        this.otpConfigRepository = otpConfigRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        log.info("GET users request");
        return ResponseEntity.ok(userRepository.findByRoleNot(UserRoles.ADMIN));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable int id
    ) {
        log.info("DELETE user id = {}", id);
        return adminService.deleteUserById(id);
    }

    @PostMapping("/otp_config")
    public ResponseEntity<String> changeConfig(
            @RequestBody @Valid OtpConfigRequest otpConfigRequest
    ) {
        log.info("POST change request");
        return adminService.changeConfig(otpConfigRequest);
    }

    @GetMapping("/otp_config")
    public ResponseEntity<OtpConfig> getOtpConfig(
            @RequestBody @Valid OtpConfigRequest otpConfigRequest
    ) {
        return ResponseEntity.ok(otpConfigRepository.findById(0).orElseThrow());
    }
}
