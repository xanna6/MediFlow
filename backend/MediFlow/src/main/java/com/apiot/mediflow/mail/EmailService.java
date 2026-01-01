package com.apiot.mediflow.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final String from;

    public EmailService(JavaMailSender mailSender,
                        @Value("${mail.from}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    public void sendAppointmentConfirmation(
            String toEmail,
            String patientName,
            LocalDateTime appointmentDate,
            String collectionPointName
    ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(toEmail);
        message.setSubject("Potwierdzenie umówienia wizyty");

        message.setText("""
                Dzień dobry %s,

                Potwierdzamy umówienie wizyty na badania laboratoryjne.

                Data wizyty: %s
                Punkt pobrań: %s

                Prosimy o punktualne przybycie.
                
                Pozdrawiamy,
                Laboratorium Diagnostyczne
                """.formatted(
                patientName,
                appointmentDate.toString(),
                collectionPointName
        ));

        mailSender.send(message);
    }
}
