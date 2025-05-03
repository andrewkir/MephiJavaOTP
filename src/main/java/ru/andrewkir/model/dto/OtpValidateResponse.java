package ru.andrewkir.model.dto;

import lombok.Getter;

@Getter
public class OtpValidateResponse {
    Boolean isValid;

    public OtpValidateResponse(boolean isValid) {
        this.isValid = isValid;
    }
}
