package com.example.fintar.repository;

import com.example.fintar.entity.CustomerDetail;
import com.example.fintar.entity.Document;
import com.example.fintar.enums.DocType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {
  Optional<Document> findByCustomerDetailAndDocType(CustomerDetail customerDetail, DocType docType);

  Optional<Document> findByCustomerDetailAndDocTypeAndIsActive(
      CustomerDetail customerDetail, DocType docType, Boolean isActive);

  Optional<Document> findByCustomerDetailAndDocTypeAndIsActiveTrue(
      CustomerDetail customerDetail, DocType docType);

  List<Document> findByCustomerDetailAndIsActiveTrue(CustomerDetail customerDetail);
}
