package ru.andrewkir.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import ru.andrewkir.model.OtpDestination;

@Data
public class RegisterRequest {
    @NotNull(message = "Имя пользователя обязательно")
    private String username;


    @NotNull(message = "Пароль обязателен")
    private String password;

    @JsonProperty("is_admin")
    private Boolean isAdmin;

    @JsonProperty("otp_destination")
    @NotNull
    private OtpDestination otpDestination;

    @JsonProperty("destination_address")
    @NotNull
    private String destinationAddress;
}