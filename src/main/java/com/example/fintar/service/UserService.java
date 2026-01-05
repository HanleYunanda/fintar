package com.example.fintar.service;

import com.example.fintar.dto.UserRequest;
import com.example.fintar.dto.UserResponse;
import com.example.fintar.entity.Role;
import com.example.fintar.entity.User;
import com.example.fintar.exception.ResourceNotFoundException;
import com.example.fintar.mapper.UserMapper;
import com.example.fintar.repository.UserRepository;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final RoleService roleService;
  private final UserMapper userMapper;

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
    if (roles.isEmpty()) throw new ResourceNotFoundException("Create base roles first");

    User user =
        User.builder()
            .username(req.getUsername())
            .email(req.getEmail())
            .password(req.getPassword())
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

  public UserResponse updateUser(UUID id, UserRequest userRequest) {
    User user = this.getUserEntity(id);
    user.setEmail(userRequest.getEmail());
    user.setUsername(userRequest.getUsername());
    user.setPassword(user.getPassword());
    return userMapper.toResponse(userRepository.save(user));
  }

  public void deleteUser(UUID id) {
    User user = this.getUserEntity(id);
    userRepository.delete(user);
  }
}
