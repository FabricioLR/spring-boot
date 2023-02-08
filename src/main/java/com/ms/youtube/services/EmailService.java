package com.ms.youtube.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ms.youtube.enums.EmailStatus;
import com.ms.youtube.models.EmailModel;
import com.ms.youtube.repositories.EmailRepository;

@Service
public class EmailService {
    @Autowired
    EmailRepository emailRepository;

    @Autowired
    private JavaMailSender emailSender;

    public List<EmailModel> findAll(){
        return emailRepository.findAll();
    }

    public EmailModel sendEmail(com.ms.youtube.models.EmailModel emailModel){
        emailModel.setDate(LocalDateTime.now());
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailModel.getEmailFrom());
            message.setTo(emailModel.getEmailTo());
            message.setSubject(emailModel.getSubject());
            message.setText(emailModel.getBody());
            emailSender.send(message);

            emailModel.setStatus(EmailStatus.SENT);
        } catch (MailException e) {
            System.out.println(e);
            emailModel.setStatus(EmailStatus.ERROR);
        }
        return emailRepository.save(emailModel);
        
    }
}
