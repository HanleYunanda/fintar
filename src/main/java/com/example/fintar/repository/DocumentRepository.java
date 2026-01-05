package com.example.fintar.repository;

import com.example.fintar.entity.CustomerDetail;
import com.example.fintar.entity.Document;
import com.example.fintar.enums.DocType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {
    Optional<Document> findByCustomerDetailAndDocType(CustomerDetail customerDetail, DocType docType);
}
