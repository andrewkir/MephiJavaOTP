package ru.andrewkir.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.andrewkir.model.dto.OtpRequest;
import ru.andrewkir.model.dto.OtpResponse;
import ru.andrewkir.model.dto.OtpValidateRequest;
import ru.andrewkir.model.dto.OtpValidateResponse;
import ru.andrewkir.service.OtpService;

@RestController
@RequestMapping("/api/otp")
@Slf4j
public class OtpController {

    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/generate")
    public OtpResponse generateOtp(
            @RequestBody @Valid OtpRequest request
    ) {
        log.info("POST generate otp request");
        return otpService.generateOtp(request);
    }

    @PostMapping("/validate")
    public ResponseEntity<OtpValidateResponse> validateOtp(
            @RequestBody @Valid OtpValidateRequest request
    ) {
        log.info("POST validate otp request");
        return otpService.validateOtp(request.getValue(), request.getOperationId());
    }
}
