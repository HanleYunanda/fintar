package com.example.fintar.service;

import com.example.fintar.entity.User;
import com.example.fintar.exception.ResourceNotFoundException;
import com.example.fintar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder passwordEncoder;

    private static final long TOKEN_EXPIRE_MINUTES = 15;

    public void processForgotPassword(String email) {

        userRepository.findByEmail(email).ifPresent(user -> {

            String token = UUID.randomUUID().toString();
            String redisKey = "reset-password:" + token;
            redisTemplate.opsForValue().set(
                    redisKey,
                    email,
                    TOKEN_EXPIRE_MINUTES,
                    TimeUnit.MINUTES
            );
            String resetLink = "http://localhost:8080/reset-password?token=" + token;

            emailService.sendForgotPasswordEmail(email, resetLink);
        });
    }

    public Boolean validateToken(String token) {
        String redisKey = "reset-password:" + token;
        String email = redisTemplate.opsForValue().get(redisKey);
        return !(email == null);
    }

    public Boolean resetPassword(String token, String newPassword) {
        String redisKey = "reset-password:" + token;

        String email = redisTemplate.opsForValue().get(redisKey);

        if (email == null) return false;

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        try {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        // Token 1x pakai → hapus
        return redisTemplate.delete(redisKey);
    }

}
