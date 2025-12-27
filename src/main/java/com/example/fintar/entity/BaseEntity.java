package com.example.fintar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;

    @CreatedBy
    @Column(updatable = false)
    protected UUID createdBy;

    @CreatedDate
    @Column(updatable = false)
    protected LocalDateTime createdAt;

    @LastModifiedBy
    protected UUID updatedBy;

    @LastModifiedDate
    protected LocalDateTime updatedAt;

    @Column(nullable = false)
    protected Boolean isDeleted = false;
}
