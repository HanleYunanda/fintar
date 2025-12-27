package com.example.fintar.repository;

import com.example.fintar.entity.CustomerDetail;
import com.example.fintar.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerDetailRepository extends JpaRepository<CustomerDetail, UUID> {
    Optional<CustomerDetail> findByUser(User user);
}
