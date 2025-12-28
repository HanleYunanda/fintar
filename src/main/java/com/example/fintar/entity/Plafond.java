package com.example.fintar.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "plafonds")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE plafonds SET is_deleted = 1 WHERE id = ?")
@SQLRestriction("is_deleted = 0")
public class Plafond extends BaseEntity implements Serializable {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Double maxAmount;

    @Column(nullable = false)
    private Integer maxTenor;

    @OneToMany(mappedBy = "plafond")
    private List<Product> products;

}
