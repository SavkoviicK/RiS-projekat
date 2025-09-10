package com.veterinarska.stanica.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void posaljiEmail(String to, String subject, String text) {
        SimpleMailMessage poruka = new SimpleMailMessage();
        poruka.setTo(to);
        poruka.setSubject(subject);
        poruka.setText(text);
        mailSender.send(poruka);
    }
}
