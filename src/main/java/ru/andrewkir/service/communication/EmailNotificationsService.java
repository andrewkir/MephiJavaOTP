package ru.andrewkir.service.communication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
@Service
public class EmailNotificationsService {
    private final String username;
    private final String password;
    private final String fromEmail;
    private final Session session;


    public EmailNotificationsService() {
        // Загрузка конфигурации
        Properties config = loadConfig();
        this.username = config.getProperty("email.username");
        this.password = config.getProperty("email.password");
        this.fromEmail = config.getProperty("email.from");
        this.session = Session.getInstance(config, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public void sendOtp(String toEmail, String code) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Your OTP Code");
            message.setText("Your verification code is: " + code);

            Transport.send(message);
            log.info("Email message sent successfully");
        } catch (MessagingException e) {
            log.error("Error sending email message");
        }
    }


    private Properties loadConfig() {
        try {
            Properties props = new Properties();
            props.load(EmailNotificationsService.class.getClassLoader()
                    .getResourceAsStream("email.properties"));
            return props;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load email configuration", e);
        }
    }
}