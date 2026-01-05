package com.example.fintar.controller;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.dto.PermissionRequest;
import com.example.fintar.dto.PermissionResponse;
import com.example.fintar.service.PermissionService;
import com.example.fintar.util.ResponseUtil;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
public class PermissionController {

  private final PermissionService permissionService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<PermissionResponse>>> index() {
    List<PermissionResponse> permissions = permissionService.getAllPermission();
    return ResponseUtil.ok(permissions, "Successfully get all permissions");
  }

  @PostMapping
  public ResponseEntity<ApiResponse<PermissionResponse>> create(
      @RequestBody @Valid PermissionRequest req) {
    PermissionResponse createdPermission = permissionService.createPermission(req);
    return ResponseUtil.ok(createdPermission, "Successfully create new permission");
  }
}
