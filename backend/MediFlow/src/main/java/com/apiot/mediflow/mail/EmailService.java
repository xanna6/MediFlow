package com.apiot.mediflow.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final String from;

    public EmailService(JavaMailSender mailSender,
                        @Value("${mail.from}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    public void sendAppointmentConfirmationMail(
            String toEmail,
            String patientName,
            LocalDateTime appointmentDate,
            String collectionPointName
    ) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(toEmail);
            helper.setSubject("Potwierdzenie umówienia wizyty");

            String html = buildHtml(
                    patientName,
                    appointmentDate,
                    collectionPointName
            );

            helper.setText(html, true); // true = HTML

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new IllegalStateException("Błąd wysyłki e-maila", e);
        }
    }

    private String buildHtml(
            String patientName,
            LocalDateTime appointmentDate,
            String collectionPointName
    ) {
        return """
            <!DOCTYPE html>
            <html lang="pl">
            <head>
                <meta charset="UTF-8">
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f4f6f8;
                        padding: 20px;
                    }
                    .container {
                        max-width: 600px;
                        margin: auto;
                        background: #ffffff;
                        border-radius: 8px;
                        padding: 24px;
                        box-shadow: 0 2px 6px rgba(0,0,0,0.1);
                    }
                    h2 {
                        color: #007acc;
                    }
                    .info {
                        margin: 16px 0;
                        padding: 12px;
                        background: #f1f7ff;
                        border-radius: 6px;
                    }
                    .footer {
                        margin-top: 24px;
                        font-size: 12px;
                        color: #666;
                        text-align: center;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h2>Potwierdzenie umówienia wizyty</h2>

                    <p>Dzień dobry <strong>%s</strong>,</p>

                    <p>Potwierdzamy umówienie wizyty na badania laboratoryjne.</p>

                    <div class="info">
                        <p><strong>Data wizyty:</strong> %s</p>
                        <p><strong>Punkt pobrań:</strong> %s</p>
                    </div>

                    <p>Prosimy o punktualne przybycie oraz zabranie dokumentu tożsamości.</p>

                    <p>Z poważaniem,<br/>
                    <strong>MediFLow</strong></p>

                    <div class="footer">
                        Wiadomość została wygenerowana automatycznie. Prosimy na nią nie odpowiadać.
                    </div>
                </div>
            </body>
            </html>
            """.formatted(
                patientName,
                appointmentDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                collectionPointName
        );
    }
}
