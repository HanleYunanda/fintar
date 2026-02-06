package com.example.fintar.mapper;

import com.example.fintar.dto.RoleRequest;
import com.example.fintar.dto.RoleResponse;
import com.example.fintar.entity.Role;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleMapper {

  private final PermissionMapper permissionMapper;

  public RoleResponse toResponse(Role role) {
    return RoleResponse.builder()
        .id(role.getId())
        .name(role.getName())
        .permissions(
            role.getPermissions() == null
                ? null
                : permissionMapper.toResponseSet(role.getPermissions()))
        .build();
  }

  public List<RoleResponse> toResponseList(List<Role> roles) {
    return roles.stream().map(this::toResponse).toList();
  }

  public Set<RoleResponse> toResponseSet(Set<Role> roles) {
    return roles.stream().map(this::toResponse).collect(Collectors.toSet());
  }

  public Role fromRequest(RoleRequest request) {
    return Role.builder().name(request.getName()).build();
  }
}
