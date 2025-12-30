package com.example.fintar.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

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

    @ManyToOne
    private Product product;

    @Column(nullable = false)
    private Double principalDebt;

    @Column(nullable = false)
    private Double outstandingDebt;

    @Column(nullable = false)
    private Integer tenor;

    @Column(nullable = false)
    private Double interestRate;

    @Column(nullable = false)
    private Double installmentPayment;

    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "loan")
    private List<LoanStatusHistory> statusHistories;

}
