package com.example.fintar.service;

import com.example.fintar.dto.RoleRequest;
import com.example.fintar.dto.RoleResponse;
import com.example.fintar.entity.Role;
import com.example.fintar.mapper.RoleMapper;
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

    @Autowired
    RoleMapper roleMapper;

    @Cacheable(value = "roles")
    public List<RoleResponse> getAllRole() {
        return roleMapper.toResponseList(roleRepository.findAll());
    }

    @Transactional
    @CacheEvict(value = "roles", allEntries = true)
    public RoleResponse createRole(RoleRequest req) {
        Role role = roleMapper.fromRequest(req);
        return roleMapper.toResponse(roleRepository.save(role));
    }

    public Set<Role> getRolesEntityByName(Set<String> names) {
        return roleRepository.findByNames(names);
    }
}
