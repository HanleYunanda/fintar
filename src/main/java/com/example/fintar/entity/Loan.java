package com.example.fintar.entity;

import com.example.fintar.enums.LoanStatus;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "loans")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE loans SET is_deleted = 1 WHERE id = ?")
@SQLRestriction("is_deleted = 0")
public class Loan extends BaseEntity {

  @ManyToOne private Product product;

  @Column(nullable = false)
  private Long principalDebt;

  @Column(nullable = false)
  private Long outstandingDebt;

  @Column(nullable = false)
  private Integer tenor;

  @Column(nullable = false)
  private Double interestRate;

  @Column(nullable = false)
  private Long installmentPayment;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private LoanStatus status;

  @OneToMany(mappedBy = "loan")
  @Builder.Default
  private List<LoanStatusHistory> statusHistories = new ArrayList<>();

  @ManyToMany
  @JoinTable(
          name = "loan_documents",
          joinColumns = @JoinColumn(name = "loan_id"),
          inverseJoinColumns = @JoinColumn(name = "document_id")
  )
  private List<Document> documents;

  private Double latitude;

  private Double longitude;

}
