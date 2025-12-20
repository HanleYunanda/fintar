package com.example.fintar.repository;

import com.example.fintar.entity.Plafond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlafondRepository extends JpaRepository<Plafond, UUID> {
}
