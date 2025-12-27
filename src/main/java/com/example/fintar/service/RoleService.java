package com.example.fintar.service;

import com.example.fintar.dto.RoleRequest;
import com.example.fintar.entity.Role;
import com.example.fintar.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Cacheable(value = "roles")
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    @Transactional
    @CacheEvict(value = "roles", allEntries = true)
    public Role createRole(RoleRequest req) {
        Role role = Role.builder()
                .name(req.getName())
                .build();
        return roleRepository.save(role);
    }

    public Set<Role> getRolesByName(Set<String> names) {
        return roleRepository.findByNames(names);
    }
}
