package ru.andrewkir.service.communication;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Slf4j
@Service
public class TelegramService {

    private final String apiKey;

    public TelegramService() {
        Properties config = loadConfig();
        this.apiKey = config.getProperty("telegram.api_key");
    }

    public void sendOtp(String destination, String code) {
        String message = String.format("Your confirmation code is: %s", code);
        String url = String.format("https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
                apiKey,
                destination,
                urlEncode(message));

        sendTelegramRequest(url);
    }
    private void sendTelegramRequest(String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    log.error("Telegram API error. Status code: {}", statusCode);
                } else {
                    log.info("Telegram message sent successfully");
                }
            }
        } catch (IOException e) {
            log.error("Error sending Telegram message: {}", e.getMessage());
        }
    }
    private static String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private Properties loadConfig() {
        try {
            Properties props = new Properties();
            props.load(TelegramService.class.getClassLoader()
                    .getResourceAsStream("telegram.properties"));
            return props;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load telegram configuration", e);
        }
    }
}
