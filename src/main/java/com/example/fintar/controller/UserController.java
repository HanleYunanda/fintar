package com.example.fintar.controller;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.dto.ChangePasswordRequest;
import com.example.fintar.dto.UserRequest;
import com.example.fintar.dto.UserResponse;
import com.example.fintar.dto.UserUpdateRequest;
import com.example.fintar.service.UserService;
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
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @GetMapping
  @PreAuthorize("hasAuthority('READ_USER')")
  public ResponseEntity<ApiResponse<List<UserResponse>>> index() {
    List<UserResponse> users = userService.getAllUser();
    return ResponseUtil.ok(users, "Successfully get all users");
  }

  @PostMapping
  @PreAuthorize("hasAuthority('CREATE_USER')")
  public ResponseEntity<ApiResponse<UserResponse>> create(@RequestBody @Valid UserRequest req) {
    UserResponse createdUser = userService.createUser(req);
    return ResponseUtil.created(createdUser, "Successfully create new user");
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('READ_USER')")
  public ResponseEntity<ApiResponse<UserResponse>> show(@PathVariable UUID id) {
    UserResponse user = userService.getUser(id);
    return ResponseUtil.ok(user, "Successfully get user");
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('UPDATE_USER')")
  public ResponseEntity<ApiResponse<UserResponse>> update(
      @PathVariable UUID id, @RequestBody @Valid UserUpdateRequest req) {
    UserResponse user = userService.updateUser(id, req);
    return ResponseUtil.ok(user, "Successfully update user");
  }

  @PatchMapping("/{id}/changePassword")
  @PreAuthorize("hasAuthority('CHANGE_PASSWORD')")
  public ResponseEntity<ApiResponse<UserResponse>> changePassword(
      @PathVariable UUID id, @RequestBody @Valid ChangePasswordRequest req) {
    UserResponse userResponse = userService.updatePassword(id, req);
    return ResponseUtil.ok(userResponse, "Successfully delete user");
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('DELETE_USER')")
  public ResponseEntity<ApiResponse<UserResponse>> delete(@PathVariable UUID id) {
    userService.deleteUser(id);
    return ResponseUtil.ok(null, "Successfully delete user");
  }
}
