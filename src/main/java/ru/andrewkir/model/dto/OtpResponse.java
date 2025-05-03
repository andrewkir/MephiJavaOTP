package ru.andrewkir.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OtpResponse {
    @JsonProperty("access_token")
    String value;

    @JsonProperty("expires_at")
    String expiresAt;

    public OtpResponse(String value, String expiresAt) {
        this.value = value;
        this.expiresAt = expiresAt;
    }
}
