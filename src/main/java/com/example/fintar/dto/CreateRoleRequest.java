package com.example.fintar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateRoleRequest {

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name cannot be blank")
    private String name;

}
