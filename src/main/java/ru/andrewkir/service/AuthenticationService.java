package ru.andrewkir.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.andrewkir.model.UserRoles;
import ru.andrewkir.model.dto.RegisterRequest;
import ru.andrewkir.model.dto.JwtResponse;
import ru.andrewkir.model.entity.User;
import ru.andrewkir.utils.JwtTokenUtil;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<JwtResponse> authenticate(String username, String password) {
        User user = userService.getByUsername(username);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.badRequest().body(new JwtResponse(null, "Неправильный пароль"));
        }

        return ResponseEntity.ok(new JwtResponse(jwtTokenUtil.generateToken(user), null));
    }

    public ResponseEntity<JwtResponse> register(RegisterRequest registerRequest) {
        String encodedPassword = new BCryptPasswordEncoder().encode(registerRequest.getPassword());
        if (registerRequest.getIsAdmin() != null && registerRequest.getIsAdmin() && userService.isAdminPresent()) {
            return ResponseEntity.badRequest().body(new JwtResponse(null, "Администратор уже зарегистрирован"));
        }

        UserRoles role = UserRoles.USER;
        if (Boolean.TRUE.equals(registerRequest.getIsAdmin())) {
            role = UserRoles.ADMIN;
        }

        var user = new User(
                registerRequest.getUsername(),
                encodedPassword,
                role,
                registerRequest.getOtpDestination(),
                registerRequest.getDestinationAddress()
        );
        userService.create(user);
        var jwt = jwtTokenUtil.generateToken(user);
        return ResponseEntity.ok(new JwtResponse(jwt, null));
    }
}