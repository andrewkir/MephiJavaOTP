package ru.andrewkir.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OtpValidateRequest {
    @NotNull(message = "Id операции обязателен")
    @JsonProperty("operation_id")
    private String operationId;

    @NotNull(message = "Код otp обязателен")
    private String value;
}
