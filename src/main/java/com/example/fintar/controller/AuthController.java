package com.example.fintar.controller;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.dto.LoginResponse;
import com.example.fintar.dto.LoginUserRequest;
import com.example.fintar.dto.RegisterUserRequest;
import com.example.fintar.entity.User;
import com.example.fintar.service.AuthService;
import com.example.fintar.service.JwtService;
import com.example.fintar.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtService jwtService;

    @GetMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(
            @RequestBody @Valid LoginUserRequest req
            ) {
        User authenticatedUser = authService.authenticate(req);
        Map<String, Object> extraClaims = jwtService.generateExtraClaims(authenticatedUser);
        String jwtToken = jwtService.generateToken(extraClaims, authenticatedUser);
        return ResponseUtil.ok(
                jwtToken,
                "Successfully log in"
        );
    }

    @GetMapping("/signup")
    public ResponseEntity<ApiResponse<User>> signup(
            @RequestBody @Valid RegisterUserRequest req
            ) {
        User registeredUser = authService.register(req);
        return ResponseUtil.created(null, "Successfully sign up");
    }

}
