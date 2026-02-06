package com.example.fintar.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

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

  @Column(name = "order_number")
  private Integer orderNumber;

  @Column(name = "next_plafond_limit")
  private Double nextPlafondLimit;

  @OneToMany(mappedBy = "plafond")
  private List<Product> products;

  @OneToMany(mappedBy = "plafond")
  private List<CustomerDetail> customerDetails;
}
