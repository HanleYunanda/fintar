package com.example.fintar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE products SET is_deleted = 1 WHERE id = ?")
@SQLRestriction("is_deleted = 0")
public class Product extends BaseEntity implements Serializable {

    @ManyToOne
    private Plafond plafond;

    @Column(nullable = false)
    private Integer tenor;

    @Column(nullable = false)
    private Double interestRate;

}
