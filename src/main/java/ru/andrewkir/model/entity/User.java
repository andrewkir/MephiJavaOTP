package ru.andrewkir.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.andrewkir.model.OtpDestination;
import ru.andrewkir.model.UserRoles;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    @Column(nullable = false)
    @Setter
    @Getter
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    @Setter
    @Getter
    private String password;

    @Column(nullable = false, name = "otp_destination")
    @Setter
    @Getter
    private OtpDestination otpDestination;

    @Column(nullable = false, name = "address")
    @JsonIgnore
    @Setter
    @Getter
    private String address;

    @Column(nullable = false)
    @Getter
    private UserRoles role;

    public User(String username, String password, UserRoles role, OtpDestination otpDestination, String address) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.otpDestination = otpDestination;
        this.address = address;
    }

    public User() {
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

}