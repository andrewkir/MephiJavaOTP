package ru.andrewkir.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.andrewkir.model.OtpDestination;

@Data
public class LoginRequest {
    @NotNull(message = "Имя пользователя обязательно")
    private String username;


    @NotNull(message = "Пароль обязателен")
    private String password;
}