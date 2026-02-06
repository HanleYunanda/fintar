package com.example.fintar.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Data;

@Data
public class UserUpdateRequest {
  @NotNull(message = "Username is required")
  @NotBlank(message = "Username cannot be blank")
  private String username;

  @NotNull(message = "Email is required")
  @NotBlank(message = "Email cannot be blank")
  @Email(message = "Format email is invalid")
  private String email;

  private Set<String> roles;
}
