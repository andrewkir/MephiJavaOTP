package ru.andrewkir.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.andrewkir.model.dto.LoginRequest;
import ru.andrewkir.model.dto.RegisterRequest;
import ru.andrewkir.model.dto.JwtResponse;
import ru.andrewkir.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginRequest registerRequest) {
        log.info("POST login request");
        return authenticationService.authenticate(
                registerRequest.getUsername(),
                registerRequest.getPassword()
        );
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@RequestBody @Valid RegisterRequest registerRequest) {
        log.info("POST register request");
        return authenticationService.register(registerRequest);
    }
}