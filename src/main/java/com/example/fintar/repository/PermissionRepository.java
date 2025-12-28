package com.example.fintar.repository;

import com.example.fintar.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    Set<Permission> findByCodeIn(Set<String> permissionCodes);
}
