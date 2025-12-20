package com.example.fintar.controller;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.dto.UserRequest;
import com.example.fintar.entity.User;
import com.example.fintar.service.UserService;
import com.example.fintar.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> index() {
        List<User> users = userService.getAllUser();
        return ResponseUtil.ok(users, "Successfully get all users");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> create(
            @RequestBody @Valid UserRequest req
    ) {
        User createdUser = userService.createUser(req);
        return ResponseUtil.created(createdUser, "Successfully create new user");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> show(
            @PathVariable UUID id
    ) {
        User user = userService.getUser(id);
        return ResponseUtil.ok(user, "Successfully get user");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> update(
            @PathVariable UUID id,
            @RequestBody @Valid UserRequest req
    ) {
        User user = userService.updateUser(id, req);
        return ResponseUtil.ok(user, "Successfully update user");
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<User>> delete(
            @PathVariable UUID id
    ) {
        userService.deleteUser(id);
        return ResponseUtil.ok(null, "Successfully delete user");
    }
}
