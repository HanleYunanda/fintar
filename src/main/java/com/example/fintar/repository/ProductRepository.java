package com.example.fintar.repository;

import com.example.fintar.entity.Plafond;
import com.example.fintar.entity.Product;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
  List<Product> findAllByPlafond(Plafond plafond);
}
