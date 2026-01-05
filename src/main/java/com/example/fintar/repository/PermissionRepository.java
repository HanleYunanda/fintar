package com.example.fintar.repository;

import com.example.fintar.entity.Permission;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {
  Set<Permission> findByCodeIn(Set<String> permissionCodes);
}
