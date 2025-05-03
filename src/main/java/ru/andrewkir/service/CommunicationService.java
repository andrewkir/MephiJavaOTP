package ru.andrewkir.service;

import org.springframework.stereotype.Service;
import ru.andrewkir.model.OtpDestination;
import ru.andrewkir.model.entity.Otp;
import ru.andrewkir.service.communication.EmailNotificationsService;
import ru.andrewkir.service.communication.FileService;
import ru.andrewkir.service.communication.SmsService;
import ru.andrewkir.service.communication.TelegramService;

@Service
public class CommunicationService {
    private final EmailNotificationsService emailNotificationsService;
    private final SmsService smsService;
    private final TelegramService telegramService;
    private final FileService fileService;

    public CommunicationService(EmailNotificationsService emailNotificationsService, SmsService smsService, TelegramService telegramService, FileService fileService) {
        this.emailNotificationsService = emailNotificationsService;
        this.smsService = smsService;
        this.telegramService = telegramService;
        this.fileService = fileService;
    }


    public void handleSendOtp(Otp otp, OtpDestination otpDestination, String address){
        switch (otpDestination) {
            case Sms -> {
                smsService.sendOtp(address, otp.getValue());
            }
            case Email -> {
                emailNotificationsService.sendOtp(address, otp.getValue());
            }
            case Telegram -> {
                telegramService.sendOtp(address, otp.getValue());
            }
            case File -> {
                fileService.sendOtp(otp.getOperationId(), otp.getValue());
            }
        }
    }
}
