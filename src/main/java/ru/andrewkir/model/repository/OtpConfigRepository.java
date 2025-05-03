package ru.andrewkir.model.repository;

import org.springframework.data.repository.CrudRepository;
import ru.andrewkir.model.entity.OtpConfig;

public interface OtpConfigRepository extends CrudRepository<OtpConfig, Integer> {
}
