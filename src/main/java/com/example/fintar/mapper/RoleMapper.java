package com.example.fintar.mapper;

import com.example.fintar.dto.RoleRequest;
import com.example.fintar.dto.UserRequest;
import com.example.fintar.dto.RoleResponse;
import com.example.fintar.entity.Role;
import com.example.fintar.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleMapper {
    public RoleResponse toResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    public List<RoleResponse> toResponseList(List<Role> roles) {
        return roles.stream()
                .map(this::toResponse)
                .toList();
    }

    public Role fromRequest(RoleRequest request) {
        return Role.builder()
                .name(request.getName())
                .build();
    }
}
