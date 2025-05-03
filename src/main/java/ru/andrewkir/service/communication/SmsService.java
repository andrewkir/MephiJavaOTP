package ru.andrewkir.service.communication;

import lombok.extern.slf4j.Slf4j;
import org.smpp.Connection;
import org.smpp.Session;
import org.smpp.TCPIPConnection;
import org.smpp.pdu.BindResponse;
import org.smpp.pdu.BindTransmitter;
import org.smpp.pdu.SubmitSM;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.util.Properties;

@Slf4j
@Service
public class SmsService {
    private final String host;
    private final Integer port;
    private final String systemId;
    private final String password;
    private final String systemType;
    private final String sourceAddress;


    public SmsService() {
        // Загрузка конфигурации
        Properties config = loadConfig();
        this.host = config.getProperty("smpp.host");
        this.port = Integer.valueOf(config.getProperty("smpp.port"));
        this.systemId = config.getProperty("smpp.system_id");
        this.systemType = config.getProperty("smpp.system_type");
        this.password = config.getProperty("smpp.password");
        this.sourceAddress = config.getProperty("smpp.source_addr");
    }

    public void sendOtp(String destination, String code) {
        Connection connection;
        Session session;

        try {
            // 1. Установка соединения
            connection = new TCPIPConnection(host, port);
            session = new Session(connection);
            // 2. Подготовка Bind Request
            BindTransmitter bindRequest = new BindTransmitter();
            bindRequest.setSystemId(systemId);
            bindRequest.setPassword(password);
            bindRequest.setSystemType(systemType);
            bindRequest.setInterfaceVersion((byte) 0x34); // SMPP v3.4
            bindRequest.setAddressRange(sourceAddress);
            // 3. Выполнение привязки
            BindResponse bindResponse = session.bind(bindRequest);
            if (bindResponse.getCommandStatus() != 0) {
                throw new Exception("Bind failed: " + bindResponse.getCommandStatus());
            }
            // 4. Отправка сообщения
            SubmitSM submitSM = new SubmitSM();
            submitSM.setSourceAddr(sourceAddress);
            submitSM.setDestAddr(destination);
            submitSM.setShortMessage("Your code: " + code);

            session.submit(submitSM);
            log.info("Sms message sent successfully");
        } catch (Exception e) {
            log.error("Error sending sms message");
        }
    }

    private Properties loadConfig() {
        try {
            Properties props = new Properties();
            props.load(SmsService.class.getClassLoader()
                    .getResourceAsStream("sms.properties"));
            return props;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load sms configuration", e);
        }
    }
}
