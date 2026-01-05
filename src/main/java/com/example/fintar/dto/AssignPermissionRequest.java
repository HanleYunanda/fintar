package com.example.fintar.dto;

import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignPermissionRequest {
  private Set<String> permissionCodes;
}
