package com.ms.youtube.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.ms.youtube.enums.EmailStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "emails")
public class EmailModel implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column()
    private String emailFrom;
    @Column()
    private String emailTo;
    @Column()
    private String subject;
    @Column(columnDefinition = "TEXT")
    private String body;
    @Column()
    private LocalDateTime date;
    @Column()
    private EmailStatus status;

    public String getBody() {
        return body;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public String getEmailFrom() {
        return emailFrom;
    }
    public String getEmailTo() {
        return emailTo;
    }
    public UUID getId() {
        return id;
    }
    public EmailStatus getStatus() {
        return status;
    }
    public String getSubject() {
        return subject;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }
    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public void setStatus(EmailStatus status) {
        this.status = status;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
}
