package com.example.fintar.repository;

import com.example.fintar.entity.Role;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
  @Query("SELECT r FROM Role r WHERE r.name IN :names")
  Set<Role> findByNames(@Param("names") Set<String> names);
}
