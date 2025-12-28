package com.example.fintar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PermissionRequest {
    @NotNull(message = "Code is required")
    @NotBlank(message = "Code cannot be blank")
    private String code;
}
