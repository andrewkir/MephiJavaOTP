package ru.andrewkir.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "otp_config")
public class OtpConfig {
    @Id
    @Getter
    @Setter
    @JsonIgnore
    private Integer id = 0;

    @Column(nullable = false)
    @Getter
    @Setter
    private Integer lifetime = 30;

    @Column(nullable = false)
    @Getter
    @Setter
    private Integer length = 4;
}