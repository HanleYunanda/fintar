package com.example.fintar.controller;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.dto.CurrentUserResponse;
import com.example.fintar.dto.LoginResponse;
import com.example.fintar.dto.LoginUserRequest;
import com.example.fintar.dto.RegisterUserRequest;
import com.example.fintar.entity.User;
import com.example.fintar.entity.UserPrincipal;
import com.example.fintar.service.AuthService;
import com.example.fintar.service.ForgotPasswordService;
import com.example.fintar.service.JwtBlacklistService;
import com.example.fintar.service.JwtService;
import com.example.fintar.util.ResponseUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final JwtBlacklistService jwtBlacklistService;
    private final ForgotPasswordService forgotPasswordService;

    @PostMapping("/login")
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

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<User>> signup(
            @RequestBody @Valid RegisterUserRequest req
            ) {
        User registeredUser = authService.register(req);
        return ResponseUtil.created(null, "Successfully sign up");
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CurrentUserResponse>> getCurrentUser(@AuthenticationPrincipal UserPrincipal currentUser) {
        CurrentUserResponse currentUserResponse = CurrentUserResponse.builder()
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
        return ResponseUtil.ok(currentUserResponse, "Successfully get logged in user");
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<String>> logout(
            HttpServletRequest request
    ) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseUtil.ok(null, "No token provided");
        }

        String token = authHeader.substring(7);
        Claims claims = jwtService.extractClaims(token);
        String jti = claims.getId();

        Long expMillis = claims.getExpiration().getTime();
        Long nowMillis = System.currentTimeMillis();
        Long ttl = expMillis - nowMillis;

        if (ttl > 0) {
            jwtBlacklistService.blacklist(jti, ttl);
        }
        return ResponseUtil.ok(null, "Successfully logged out");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> logout(
            @RequestParam String email
    ) {
        forgotPasswordService.processForgotPassword(email);
        return ResponseUtil.ok(null, "If the email is registered, a reset link has been sent");
    }

    @GetMapping("/reset-password")
    public ResponseEntity<ApiResponse<Object>> validateToken(@RequestParam String token) {
        Boolean isValid = forgotPasswordService.validateToken(token);
        if(!isValid) return ResponseUtil.error(HttpStatus.BAD_REQUEST, "Token invalid");
        return ResponseUtil.ok(null, "Token valid");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Object>> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword
    ) {
        Boolean isSuccess = forgotPasswordService.resetPassword(token, newPassword);
        if(!isSuccess) return ResponseUtil.error(HttpStatus.BAD_REQUEST, "Failed to reset password");
        return ResponseUtil.ok(null, "Successfully reset password");
    }
}
