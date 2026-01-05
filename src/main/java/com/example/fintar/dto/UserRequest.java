package com.example.fintar.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Set;
import lombok.Data;

@Data
public class UserRequest {

  @NotNull(message = "Username is required")
  @NotBlank(message = "Username cannot be blank")
  private String username;

  @NotNull(message = "Email is required")
  @NotBlank(message = "Email cannot be blank")
  @Email(message = "Format email is invalid")
  private String email;

  @NotNull(message = "Password is required")
  @NotBlank(message = "Password cannot be blank")
  @Pattern(
      regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$",
      message = "Password format is invalid")
  private String password;

  private Set<String> roles;
}
