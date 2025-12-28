package com.example.fintar.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class CurrentUserResponse {
    private String username;
    private String email;
    private Set<String> roles;
    private Set<String> permissions;
}
