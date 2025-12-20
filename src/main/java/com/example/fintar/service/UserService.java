package com.example.fintar.service;

import com.example.fintar.dto.UserRequest;
import com.example.fintar.entity.User;
import com.example.fintar.exception.ResourceNotFoundException;
import com.example.fintar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public User createUser(UserRequest req) {
        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(req.getPassword())
                .isActive(true)
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
