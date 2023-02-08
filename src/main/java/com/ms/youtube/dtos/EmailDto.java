package com.ms.youtube.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailDto {
    @Email(message = "required a valid email")
    @NotBlank(message = "from user is required")
    private String emailFrom;
    @Email(message = "required a valid email")
    @NotBlank(message = "to user is required")
    private String emailTo;
    @NotBlank(message = "subject user is required")
    private String subject;
    @NotBlank(message = "body user is required")
    private String body;
}
