package com.example.fintar.service;

import com.example.fintar.dto.AssignPermissionRequest;
import com.example.fintar.dto.AssignPermissionResponse;
import com.example.fintar.dto.RoleRequest;
import com.example.fintar.dto.RoleResponse;
import com.example.fintar.entity.Permission;
import com.example.fintar.entity.Role;
import com.example.fintar.exception.ResourceNotFoundException;
import com.example.fintar.mapper.PermissionMapper;
import com.example.fintar.mapper.RoleMapper;
import com.example.fintar.repository.RoleRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

  private final RoleRepository roleRepository;
  private final PermissionService permissionService;
  private final RoleMapper roleMapper;
  private final PermissionMapper permissionMapper;

  @Cacheable(value = "roles")
  public List<RoleResponse> getAllRole() {
    return roleMapper.toResponseList(roleRepository.findAll());
  }

  @Transactional
  @CacheEvict(value = "roles", allEntries = true)
  public RoleResponse createRole(RoleRequest req) {
    Role role = roleMapper.fromRequest(req);
    return roleMapper.toResponse(roleRepository.save(role));
  }

  public Set<Role> getRolesEntityByName(Set<String> names) {
    return roleRepository.findByNames(names);
  }

  public Role getRoleEntityById(UUID id) {
    Optional<Role> role = roleRepository.findById(id);
    if (role.isEmpty()) throw new ResourceNotFoundException("Role with id " + id + " not found");
    return role.get();
  }

  public AssignPermissionResponse assignPermissions(Role role, AssignPermissionRequest req) {
    // Get permissions from DB
    Set<Permission> permissions =
        permissionService.getAllPermissionByCode(req.getPermissionCodes());
    role.setPermissions(permissions);
    Role updatedRole = roleRepository.save(role);
    return AssignPermissionResponse.builder()
        .role(roleMapper.toResponse(updatedRole))
        .permissions(permissionMapper.toResponseSet(updatedRole.getPermissions()))
        .build();
  }
}
