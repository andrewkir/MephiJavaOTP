package ru.andrewkir.model.repository;

import org.springframework.data.repository.CrudRepository;
import ru.andrewkir.model.UserRoles;
import ru.andrewkir.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByRole(UserRoles role);

    List<User> findByRoleNot(UserRoles role);
}