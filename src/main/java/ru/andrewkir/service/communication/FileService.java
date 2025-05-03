package ru.andrewkir.service.communication;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;

@Slf4j
@Service
public class FileService {

    private final String path;

    public FileService() {
        Properties config = loadConfig();
        this.path = config.getProperty("otp_path");
    }

    public void sendOtp(String destination, String code) {
        try {
            Path dirPath = Paths.get(path);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            try (FileWriter writer = new FileWriter(path + "/otp.txt", false)) {
                writer.write(String.format("%s OTP = %s", destination, code));
            } catch (IOException e) {
                throw new RuntimeException("File write error", e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Properties loadConfig() {
        try {
            Properties props = new Properties();
            props.load(SmsService.class.getClassLoader()
                    .getResourceAsStream("files.properties"));
            return props;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load files configuration", e);
        }
    }
}
