package com.example.fintar.dto;

import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrentUserResponse {
  private String username;
  private String email;
  private Set<String> roles;
  private Set<String> permissions;
}
