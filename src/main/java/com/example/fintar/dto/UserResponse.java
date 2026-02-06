package com.example.fintar.dto;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
  private UUID id;
  private String username;
  private String email;
  private List<RoleResponse> roles;
  //  private List<PermissionResponse> permissions;
}
