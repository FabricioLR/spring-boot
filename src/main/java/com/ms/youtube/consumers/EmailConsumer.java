package com.ms.youtube.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.ms.youtube.dtos.EmailDto;
import com.ms.youtube.models.EmailModel;
import com.ms.youtube.services.EmailService;

@Component
public class EmailConsumer {
    
    @Autowired
    EmailService emailService;

    @RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
    public void listen(@Payload EmailDto emailDto){
        EmailModel emailModel = new EmailModel();
        BeanUtils.copyProperties(emailDto, emailModel);
        emailService.sendEmail(emailModel);
        System.out.println("Email Status: " + emailModel.getStatus().toString());
    }
}
