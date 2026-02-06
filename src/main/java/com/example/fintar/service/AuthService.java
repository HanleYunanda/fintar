package com.example.fintar.service;

import com.example.fintar.dto.*;
import com.example.fintar.entity.User;
import com.example.fintar.entity.UserPrincipal;
import com.example.fintar.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final RoleService roleService;
  private final CustomerDetailService customerDetailService;

  public User register(RegisterUserRequest req) {
    User user =
        User.builder()
            .email(req.getEmail())
            .username(req.getUsername())
            .password(passwordEncoder.encode(req.getPassword()))
            .roles(roleService.getRolesEntityByName(Set.of("CUSTOMER")))
            .isActive(true)
            .build();
    return userRepository.save(user);
  }

  public UserPrincipal authenticate(LoginUserRequest req) {
    Authentication auth =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));

    UserPrincipal principal = (UserPrincipal) auth.getPrincipal();

    if (req.getFcmToken() != null && !req.getFcmToken().isEmpty()) {
      User user = principal.getUser();
      user.setFcmToken(req.getFcmToken());
      userRepository.save(user);
    }

    return principal;
  }

  public UserPrincipal loginWithGoogle(GoogleLoginRequest req) {
    try {
      FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(req.getIdToken());
      String email = decodedToken.getEmail();
      String name = decodedToken.getName();

      // Find user by email
      return userRepository
          .findByEmail(email)
          .map(
              user -> {
                if (req.getFcmToken() != null && !req.getFcmToken().isEmpty()) {
                  user.setFcmToken(req.getFcmToken());
                  userRepository.save(user);
                }
                return UserPrincipal.create(user);
              })
          .orElseGet(
              () -> {
                // Register new user
                User newUser =
                    User.builder()
                        .email(email)
                        .username(email) // Use email as username for Google login
                        .password(
                            passwordEncoder.encode(
                                java.util.UUID.randomUUID().toString())) // Random password
                        .roles(roleService.getRolesEntityByName(Set.of("CUSTOMER")))
                        .isActive(true)
                        .fcmToken(req.getFcmToken())
                        .build();
                User registeredUser = userRepository.save(newUser);
                CustomerDetailResponse customerDetail =
                    customerDetailService.createCustomerDetail(
                        CustomerDetailRequest.builder().userId(registeredUser.getId()).build());
                return UserPrincipal.create(registeredUser);
              });

    } catch (Exception e) {
      throw new RuntimeException("Invalid Google ID Token", e);
    }
  }

  public UserPrincipal getAuthenticatedUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return (UserPrincipal) auth.getPrincipal();
  }
}
