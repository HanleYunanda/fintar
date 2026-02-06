package com.example.fintar.repository;

import com.example.fintar.entity.Plafond;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlafondRepository extends JpaRepository<Plafond, UUID> {
  Optional<Plafond> findByName(String name);

  java.util.List<Plafond> findByOrderNumberIsNotNullOrderByOrderNumberAsc();

  Optional<Plafond> findByOrderNumber(Integer orderNumber);

  @org.springframework.data.jpa.repository.Query("SELECT MAX(p.orderNumber) FROM Plafond p")
  Integer findMaxOrderNumber();
}
