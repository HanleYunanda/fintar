package com.example.fintar.service;

import com.example.fintar.dto.CreateRoleRequest;
import com.example.fintar.entity.Role;
import com.example.fintar.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    public Role createRole(CreateRoleRequest req) {
        Role role = Role.builder()
                .name(req.getName())
                .isActive(true)
                .build();
        return roleRepository.save(role);
    }

    public Set<Role> getRolesByName(Set<String> names) {
        return roleRepository.findByNames(names);
    }
}
