package com.example.fintar.service;

import com.example.fintar.dto.PermissionRequest;
import com.example.fintar.dto.PermissionResponse;
import com.example.fintar.entity.Permission;
import com.example.fintar.mapper.PermissionMapper;
import com.example.fintar.repository.PermissionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public List<PermissionResponse> getAllPermission() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissionMapper.toResponseList(permissions);
    }


    public PermissionResponse createPermission(PermissionRequest req) {
        Permission permission = permissionMapper.fromRequest(req);
        return permissionMapper.toResponse(permissionRepository.save(permission));
    }

    public Set<Permission> getAllPermissionByCode(Set<String> permissionCodes) {
        return permissionRepository.findByCodeIn(permissionCodes);
    }
}
