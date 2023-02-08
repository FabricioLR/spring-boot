package com.ms.youtube.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleDto {
    @NotBlank(message = "role is required")
    private String roleName;
}
