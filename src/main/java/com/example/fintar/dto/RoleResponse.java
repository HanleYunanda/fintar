package com.example.fintar.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleResponse {
  private UUID id;
  private String name;
  private Set<PermissionResponse> permissions = new HashSet<>();
}
