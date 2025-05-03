package ru.andrewkir.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.andrewkir.model.entity.OtpConfig;
import ru.andrewkir.model.repository.OtpConfigRepository;

@Component
class OtpConfigInit implements ApplicationRunner {

    private final OtpConfigRepository otpConfigRepository;

    @Autowired
    public OtpConfigInit(OtpConfigRepository otpConfigRepository) {
        this.otpConfigRepository = otpConfigRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!otpConfigRepository.existsById(0)) {
            otpConfigRepository.save(new OtpConfig());
        }
    }
}