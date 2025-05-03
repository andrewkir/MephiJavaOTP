package ru.andrewkir.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtResponse {
    @JsonProperty("access_token")
    String token;

    @JsonProperty("error")
    String error;

    public JwtResponse(String token, String error) {
        this.token = token;
        this.error = error;
    }
}
