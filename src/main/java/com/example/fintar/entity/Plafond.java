package com.example.fintar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "plafonds")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Plafond {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Double maxAmount;

    @Column(nullable = false)
    private Integer maxTenor;

    @Column(columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String productSelection; // JSON string

    @Column(nullable = false)
    private UUID createdBy;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private UUID updatedBy;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    /*
        Data Inside productSelection
        [
            {
                "interestRate":0.03,
                "tenor":2
            },
            {
                "interestRate":0.02,
                "tenor":3
            }
        ]
    */

}
