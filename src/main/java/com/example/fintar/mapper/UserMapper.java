package com.example.fintar.mapper;

import com.example.fintar.dto.UserRequest;
import com.example.fintar.dto.UserResponse;
import com.example.fintar.entity.Permission;
import com.example.fintar.entity.Role;
import com.example.fintar.entity.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.expression.Sets;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final RoleMapper roleMapper;
  private final PermissionMapper permissionMapper;

  public UserResponse toResponse(User user) {
    Set<Role> roles = user.getRoles();
//    Set<Permission> permissions = new HashSet<>();
//    for (Role role : roles) {
//      Set<Permission> rolePermissions = role.getPermissions();
//        permissions.addAll(rolePermissions);
//    }

    return UserResponse.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .roles(roleMapper.toResponseList(user.getRoles().stream().toList()))
//        .permissions(permissionMapper.toResponseList(permissions.stream().toList()))
        .build();
  }

  public List<UserResponse> toResponseList(List<User> users) {
    return users.stream().map(this::toResponse).toList();
  }

  public User fromRequest(UserRequest request) {
    return User.builder()
        .username(request.getUsername())
        .email(request.getEmail())
        .password(request.getPassword())
        .build();
  }
}
