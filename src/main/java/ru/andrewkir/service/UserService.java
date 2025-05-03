package ru.andrewkir.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.andrewkir.model.UserRoles;
import ru.andrewkir.model.entity.User;
import ru.andrewkir.model.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }
        return save(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    public User getById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public boolean isAdminPresent() {
        return userRepository.existsByRole(UserRoles.ADMIN);
    }

    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
}
