package com.example.fintar.service;

import com.example.fintar.dto.ChangePasswordRequest;
import com.example.fintar.dto.UserRequest;
import com.example.fintar.dto.UserResponse;
import com.example.fintar.dto.UserUpdateRequest;
import com.example.fintar.entity.Role;
import com.example.fintar.entity.User;
import com.example.fintar.entity.UserPrincipal;
import com.example.fintar.exception.BusinessValidationException;
import com.example.fintar.exception.ResourceNotFoundException;
import com.example.fintar.mapper.UserMapper;
import com.example.fintar.repository.UserRepository;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final RoleService roleService;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  public List<UserResponse> getAllUser() {
    return userMapper.toResponseList(userRepository.findAll());
  }

  public UserResponse createUser(UserRequest req) {
    // Jika roles di request kosong kasih default role CUSTOMER
    if (req.getRoles().isEmpty()) {
      req.setRoles(Set.of("CUSTOMER"));
    }

    // Ambil roles dari DB
    Set<Role> roles = roleService.getRolesEntityByName(req.getRoles());
    if (roles.isEmpty()) throw new ResourceNotFoundException("Role not found");

    // Cek email unique
    if (userRepository.findByEmail(req.getEmail()).isPresent())
      throw new BusinessValidationException("Email already exist");

    User user =
        User.builder()
            .username(req.getUsername())
            .email(req.getEmail())
            .password(passwordEncoder.encode(req.getPassword()))
            .isActive(true)
            .roles(roles)
            .build();

    user = userRepository.save(user);
    return userMapper.toResponse(user);
  }

  public User getUserEntity(UUID id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isEmpty()) throw new ResourceNotFoundException("User with id " + id + " not found");
    return user.get();
  }

  public UserResponse getUser(UUID id) {
    return userMapper.toResponse(this.getUserEntity(id));
  }

  public UserResponse updateUser(UUID id, UserUpdateRequest userRequest) {
    User user = this.getUserEntity(id);
    user.setEmail(userRequest.getEmail());
    user.setUsername(userRequest.getUsername());

    // Ambil roles dari DB
    Set<Role> roles = roleService.getRolesEntityByName(userRequest.getRoles());
    if (roles.isEmpty()) throw new ResourceNotFoundException("Role not found");
    user.setRoles(roles);

    return userMapper.toResponse(userRepository.save(user));
  }

  public void deleteUser(UUID id) {
    User user = this.getUserEntity(id);
    userRepository.delete(user);
  }

  public UserResponse updatePassword(UUID id, ChangePasswordRequest request) {

    User user = this.getUserEntity(id);

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

    // Check if user belong to logged in user
    if (!Objects.equals(userPrincipal.getUser().getId(), user.getId()))
      throw new BusinessValidationException("Cannot change other user password");

    // Check if old password valid
    if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
      throw new BusinessValidationException("Old Password Invalid");
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));

    return userMapper.toResponse(userRepository.save(user));
  }
}
