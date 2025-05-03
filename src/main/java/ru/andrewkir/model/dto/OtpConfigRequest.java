package ru.andrewkir.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class OtpConfigRequest {
    @Min(value = 30, message = "Lifetime must be at least 30 seconds")
    @Max(value = 24 * 60, message = "Lifetime cannot exceed 24 hours")
    private Integer lifetime;

    @Min(value = 2, message = "Length must be at least 2 characters")
    @Max(value = 10, message = "Length cannot exceed 10 characters")
    private Integer length;
}