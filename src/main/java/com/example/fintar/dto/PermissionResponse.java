package com.example.fintar.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PermissionResponse {
  private UUID id;
  private String code;
}
