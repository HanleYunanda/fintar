package com.example.fintar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginResponse {
  private String username;
  private String token;
  private Long expiresIn;
}
