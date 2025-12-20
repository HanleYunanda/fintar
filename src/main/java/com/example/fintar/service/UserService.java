package com.example.fintar.service;

import com.example.fintar.dto.UserRequest;
import com.example.fintar.entity.Role;
import com.example.fintar.entity.User;
import com.example.fintar.exception.ResourceNotFoundException;
import com.example.fintar.repository.RoleRepository;
import com.example.fintar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public User createUser(UserRequest req) {
        // Jika roles di request kosong kasih default role CUSTOMER
        if(req.getRoles().isEmpty()) {
            req.setRoles(Set.of("CUSTOMER"));
        }

        // Ambil roles dari DB
        Set<Role> roles = roleService.getRolesByName(req.getRoles());
        if(roles.isEmpty()) throw new ResourceNotFoundException("Create base roles first");

        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(req.getPassword())
                .isActive(true)
                .roles(roles)
                .build();
        return userRepository.save(user);
    }

    public User getUser(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) throw new ResourceNotFoundException("User with id " + id + " not found");
        return user.get();
    }

    public User updateUser(UUID id, UserRequest userRequest) {
        User user = this.getUser(id);
        user.setEmail(userRequest.getEmail());
        user.setUsername(userRequest.getUsername());
        user.setPassword(user.getPassword());
        return userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        User user = this.getUser(id);
        userRepository.delete(user);
    }
}
