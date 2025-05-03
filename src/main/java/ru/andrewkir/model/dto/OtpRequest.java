package ru.andrewkir.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.andrewkir.model.OtpDestination;

@Data
public class OtpRequest {
    @NotNull(message = "Id операции обязателен")
    @JsonProperty("operation_id")
    private String operationId;
}
