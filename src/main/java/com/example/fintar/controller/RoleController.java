package com.example.fintar.controller;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.dto.AssignPermissionRequest;
import com.example.fintar.dto.AssignPermissionResponse;
import com.example.fintar.dto.RoleRequest;
import com.example.fintar.dto.RoleResponse;
import com.example.fintar.entity.Role;
import com.example.fintar.service.RoleService;
import com.example.fintar.util.ResponseUtil;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

  private final RoleService roleService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<RoleResponse>>> index() {
    List<RoleResponse> roles = roleService.getAllRole();
    return ResponseUtil.ok(roles, "Successfully get all roles");
  }

  @PostMapping
  public ResponseEntity<ApiResponse<RoleResponse>> create(@RequestBody @Valid RoleRequest req) {
    RoleResponse createdRole = roleService.createRole(req);
    return ResponseUtil.created(createdRole, "Successfully create new role");
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<RoleResponse>> show(
          @PathVariable UUID id
  ) {
    RoleResponse role = roleService.getRoleById(id);
    return ResponseUtil.ok(role, "Successfully create new role");
  }

  @PostMapping("/{id}/assign-permissions")
  public ResponseEntity<ApiResponse<AssignPermissionResponse>> create(
      @RequestBody AssignPermissionRequest req, @PathVariable UUID id) {
    // Get Role
    Role role = roleService.getRoleEntityById(id);
    AssignPermissionResponse assignPermissionResponse = roleService.assignPermissions(role, req);
    return ResponseUtil.created(
        assignPermissionResponse, "Successfully assign permissions to role: " + role.getName());
  }
}
