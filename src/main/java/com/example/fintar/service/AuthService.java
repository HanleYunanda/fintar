package com.example.fintar.service;

import com.example.fintar.dto.LoginUserRequest;
import com.example.fintar.dto.RegisterUserRequest;
import com.example.fintar.entity.Role;
import com.example.fintar.entity.User;
import com.example.fintar.entity.UserPrincipal;
import com.example.fintar.repository.UserRepository;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleService roleService;

    public User register(RegisterUserRequest req) {
        User user = User.builder()
                .email(req.getEmail())
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .roles(roleService.getRolesEntityByName(Set.of("CUSTOMER")))
                .isActive(true)
                .build();
        return userRepository.save(user);
    }

    public UserPrincipal authenticate(LoginUserRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getUsername(),
                        req.getPassword()
                )
        );
        return (UserPrincipal) auth.getPrincipal();
    }

    public void getAuthenticatedUser() {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth);
    }
}
