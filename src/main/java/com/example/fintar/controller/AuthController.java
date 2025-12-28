package com.example.fintar.controller;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.dto.CurrentUserResponse;
import com.example.fintar.dto.LoginResponse;
import com.example.fintar.dto.LoginUserRequest;
import com.example.fintar.dto.RegisterUserRequest;
import com.example.fintar.entity.User;
import com.example.fintar.entity.UserPrincipal;
import com.example.fintar.service.AuthService;
import com.example.fintar.service.JwtService;
import com.example.fintar.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @GetMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(
            @RequestBody @Valid LoginUserRequest req
            ) {
        UserPrincipal authenticatedUser = authService.authenticate(req);
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

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public CurrentUserResponse getCurrentUser(@AuthenticationPrincipal UserPrincipal currentUser) {
        return CurrentUserResponse.builder()
                .username(currentUser.getUsername())
                .email(currentUser.getUser().getEmail())
                .roles(currentUser.getAuthorities().stream()
                        .filter(a -> a.getAuthority().startsWith("ROLE_"))
                        .map(a -> a.getAuthority().substring(5)) // hapus prefix "ROLE_"
                        .collect(Collectors.toSet()))
                .permissions(currentUser.getAuthorities().stream()
                        .filter(a -> !a.getAuthority().startsWith("ROLE_"))
                        .map(a -> a.getAuthority())
                        .collect(Collectors.toSet()))
                .build();
    }

}
