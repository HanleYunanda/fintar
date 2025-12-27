package com.example.fintar.controller;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.dto.RoleRequest;
import com.example.fintar.dto.RoleResponse;
import com.example.fintar.entity.Role;
import com.example.fintar.service.RoleService;
import com.example.fintar.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> index() {
        List<RoleResponse> roles = roleService.getAllRole();
        return ResponseUtil.ok(roles, "Successfully get all roles");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> create(
            @RequestBody @Valid RoleRequest req
    ) {
        RoleResponse createdRole = roleService.createRole(req);
        return ResponseUtil.created(createdRole, "Successfully create new role");
    }

}
