package com.example.fintar.mapper;

import com.example.fintar.dto.PermissionRequest;
import com.example.fintar.dto.PermissionResponse;
import com.example.fintar.entity.Permission;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {
  public PermissionResponse toResponse(Permission permission) {
    return PermissionResponse.builder().id(permission.getId()).code(permission.getCode()).build();
  }

  public List<PermissionResponse> toResponseList(List<Permission> permissions) {
    return permissions.stream().map(this::toResponse).toList();
  }

  public Set<PermissionResponse> toResponseSet(Set<Permission> permissions) {
    return permissions.stream().map(this::toResponse).collect(Collectors.toSet());
  }

  public Permission fromRequest(PermissionRequest request) {
    return Permission.builder().code(request.getCode()).build();
  }
}
