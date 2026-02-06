package com.example.fintar.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleLoginRequest {
  @NotBlank(message = "ID Token is required")
  private String idToken;

  private String fcmToken;
}
