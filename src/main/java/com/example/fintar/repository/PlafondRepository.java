package com.example.fintar.repository;

import com.example.fintar.entity.Plafond;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlafondRepository extends JpaRepository<Plafond, UUID> {}
