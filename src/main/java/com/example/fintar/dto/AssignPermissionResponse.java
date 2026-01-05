package com.example.fintar.dto;

import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignPermissionResponse {
  private RoleResponse role;
  private Set<PermissionResponse> permissions;
}
