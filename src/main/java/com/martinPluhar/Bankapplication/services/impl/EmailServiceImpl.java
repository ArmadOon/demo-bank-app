package com.martinPluhar.Bankapplication.services.impl;

import com.martinPluhar.Bankapplication.dto.EmailDetails;
import com.martinPluhar.Bankapplication.services.intfc.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Implementace rozhraní {@link EmailService} pro odesílání e-mailů pomocí JavaMailSender.
 */
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    // Získání e-mailové adresy odesílatele z konfigurace Spring Boot.
    @Getter
    @Value("${spring.mail.username}")
    private String senderEmail;

    /**
     * Metoda pro odeslání e-mailového upozornění s jednoduchým textovým obsahem.
     *
     * @param emailDetails Objekt obsahující informace o e-mailu (příjemce, předmět, tělo zprávy).
     */
    @Override
    public void sendEmailAlert(EmailDetails emailDetails) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderEmail);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setText(emailDetails.getMessageBody());
            mailMessage.setSubject(emailDetails.getSubject());

            // Odeslání e-mailu
            javaMailSender.send(mailMessage);
            System.out.println("Email byl v pořádku odeslán!");
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metoda pro odeslání e-mailu s přílohou.
     *
     * @param emailDetails Objekt obsahující informace o e-mailu (příjemce, předmět, tělo zprávy, příloha).
     */
    @Override
    public void sendEmailWithAttachment(EmailDetails emailDetails) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(senderEmail);
            helper.setTo(emailDetails.getRecipient());
            helper.setSubject(emailDetails.getSubject());
            helper.setText(emailDetails.getMessageBody());

            // Přidání přílohy
            FileSystemResource file = new FileSystemResource(new File(emailDetails.getAttachment()));
            helper.addAttachment("MyStatement.pdf", file);

            // Odeslání e-mailu s přílohou
            javaMailSender.send(message);
            System.out.println("E-mail s přílohou byl odeslán!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}