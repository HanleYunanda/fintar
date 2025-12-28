package com.example.fintar.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AssignPermissionResponse {
    private RoleResponse role;
    private Set<PermissionResponse> permissions;
}
