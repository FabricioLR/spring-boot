package com.ms.youtube.controllers;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ms.youtube.dtos.EmailDto;
import com.ms.youtube.models.EmailModel;
import com.ms.youtube.services.EmailService;

import jakarta.validation.Valid;

@RestController
public class EmailController {
    @Autowired
    EmailService emailService;

    @GetMapping("/getEmails")
    public ResponseEntity<List<EmailModel>> getEmails(){
        return ResponseEntity.status(HttpStatus.OK).body(emailService.findAll());
    }

    @PostMapping("/saveEmail")
    public ResponseEntity<EmailModel> saveEmail(@RequestBody @Valid EmailDto emailDto){
        EmailModel emailModel = new EmailModel();
        BeanUtils.copyProperties(emailDto, emailModel);
        emailService.sendEmail(emailModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(emailModel);
    }
}
